from flask import Flask, jsonify, request
from flask_cors import CORS
import requests
import os
from datetime import datetime
import json
import logging

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

app = Flask(__name__)
CORS(app)  # Enable CORS for all routes to allow Android app access

# You'll need to get your API token from https://developer.clashofclans.com/
COC_API_TOKEN = os.environ.get('COC_API_TOKEN', 'YOUR_API_TOKEN_HERE')
COC_API_BASE = 'https://cocproxy.royaleapi.dev/v1'

# Headers for Clash of Clans API
HEADERS = {
    'Authorization': f'Bearer {COC_API_TOKEN}',
    'Accept': 'application/json'
}

def get_th_emoji(level):
    """Get emoji for townhall level"""
    th_emojis = {
        1: "🏠", 2: "🏡", 3: "🏘️", 4: "🏢", 5: "🏥", 6: "🏰", 7: "🕌", 8: "🏯",
        9: "🛕", 10: "🏛️", 11: "🗼", 12: "🏟️", 13: "🗽", 14: "🗿", 15: "🏺", 16: "🔧", 17: "🔨"
    }
    return th_emojis.get(level, "🏠")

def format_clan_tag(tag):
    """Format clan tag to ensure it starts with #"""
    if not tag.startswith('#'):
        tag = '#' + tag
    return tag.upper()

def calculate_time_remaining(end_time):
    """Calculate time remaining for war"""
    try:
        # Parse the timestamp (format: 20240101T123000.000Z)
        end_dt = datetime.strptime(end_time, "%Y%m%dT%H%M%S.%fZ")
        now = datetime.utcnow()
        
        if end_dt > now:
            diff = end_dt - now
            hours = diff.total_seconds() // 3600
            minutes = (diff.total_seconds() % 3600) // 60
            
            if hours > 0:
                return f"{int(hours)}h {int(minutes)}m", "remaining"
            else:
                return f"{int(minutes)}m", "remaining"
        else:
            return None, None
    except:
        return None, None

@app.route('/')
def index():
    """API status endpoint"""
    return jsonify({
        'service': 'ClashBerry API',
        'status': 'running',
        'version': '1.0.0',
        'endpoints': {
            'war_data': '/api/war/<clan_tag>',
            'clan_info': '/api/clan/<clan_tag>',
            'health': '/health'
        }
    })

@app.route('/health')
def health_check():
    """Health check endpoint for monitoring"""
    return jsonify({
        'status': 'healthy',
        'timestamp': datetime.utcnow().isoformat(),
        'api_token_configured': COC_API_TOKEN != 'YOUR_API_TOKEN_HERE'
    })

@app.route('/api/war/<clan_tag>')
def get_war_data(clan_tag):
    """Get current war data for a clan"""
    try:
        logger.info(f"Fetching war data for clan: {clan_tag}")
        
        # Format the clan tag
        formatted_tag = format_clan_tag(clan_tag)
        encoded_tag = requests.utils.quote(formatted_tag, safe='')
        
        # First, get clan info to check if war log is public
        clan_url = f"{COC_API_BASE}/clans/{encoded_tag}"
        clan_response = requests.get(clan_url, headers=HEADERS, timeout=10)
        
        if clan_response.status_code == 404:
            logger.warning(f"Clan not found: {formatted_tag}")
            return jsonify({
                'error': 'clan_not_found',
                'message': 'Clan not found. Please check the clan tag.'
            }), 404
        
        if clan_response.status_code != 200:
            logger.error(f"Clan API error: {clan_response.status_code}")
            return jsonify({
                'error': 'api_error',
                'message': f'API Error: {clan_response.status_code}'
            }), clan_response.status_code
        
        clan_data = clan_response.json()
        
        # Check if war log is public
        if not clan_data.get('isWarLogPublic', False):
            logger.info(f"Private war log for clan: {formatted_tag}")
            return jsonify({
                'error': 'private_war_log',
                'message': 'This clan has a private war log.',
                'clan': {
                    'name': clan_data.get('name'),
                    'tag': clan_data.get('tag'),
                    'badge': clan_data.get('badgeUrls', {}).get('medium', '')
                }
            }), 403
        
        # Get current war data
        war_url = f"{COC_API_BASE}/clans/{encoded_tag}/currentwar"
        war_response = requests.get(war_url, headers=HEADERS, timeout=10)
        
        if war_response.status_code != 200:
            logger.error(f"War API error: {war_response.status_code}")
            return jsonify({
                'error': 'api_error',
                'message': f'War API Error: {war_response.status_code}'
            }), war_response.status_code
        
        war_data = war_response.json()
        
        # Check if clan is in war
        if war_data.get('state') == 'notInWar':
            logger.info(f"Clan not in war: {formatted_tag}")
            return jsonify({
                'error': 'not_in_war',
                'message': 'This clan is not currently in a war.',
                'clan': {
                    'name': clan_data.get('name'),
                    'tag': clan_data.get('tag'),
                    'badge': clan_data.get('badgeUrls', {}).get('medium', '')
                }
            }), 404
        
        # Process war data
        processed_data = process_war_data(war_data)
        logger.info(f"Successfully processed war data for clan: {formatted_tag}")
        
        return jsonify(processed_data)
        
    except requests.RequestException as e:
        logger.error(f"Network error: {str(e)}")
        return jsonify({
            'error': 'network_error',
            'message': 'Failed to connect to Clash of Clans API'
        }), 503
        
    except Exception as e:
        logger.error(f"Server error: {str(e)}")
        return jsonify({
            'error': 'server_error',
            'message': f'Server error: {str(e)}'
        }), 500

def process_war_data(war_data):
    """Process raw war data into formatted response"""
    
    # Calculate time remaining
    time_remaining, status = calculate_time_remaining(war_data.get('endTime', ''))
    
    # Determine war type (regular war vs CWL)
    war_type = 'cwl' if war_data.get('isCWL', False) else 'regular'
    
    processed_data = {
        'state': war_data.get('state'),
        'teamSize': war_data.get('teamSize'),
        'warType': war_type,
        'preparationStartTime': war_data.get('preparationStartTime'),
        'startTime': war_data.get('startTime'),
        'endTime': war_data.get('endTime'),
        'timeRemaining': time_remaining,
        'clan': process_clan_data(war_data.get('clan', {})),
        'opponent': process_clan_data(war_data.get('opponent', {}))
    }
    
    return processed_data

def process_clan_data(clan_raw):
    """Process clan data from war response"""
    members = []
    
    for member in clan_raw.get('members', []):
        # Process attacks
        attacks = []
        attacks_used = 0
        
        for attack in member.get('attacks', []):
            attacks.append({
                'stars': attack.get('stars', 0),
                'destructionPercentage': attack.get('destructionPercentage', 0),
                'defenderTag': attack.get('defenderTag', ''),
                'order': attack.get('order', 0)
            })
            attacks_used += 1
        
        members.append({
            'tag': member.get('tag'),
            'name': member.get('name'),
            'townhallLevel': member.get('townhallLevel'),
            'thEmoji': get_th_emoji(member.get('townhallLevel')),
            'mapPosition': member.get('mapPosition'),
            'attacks': attacks,
            'attacksUsed': attacks_used,
            'opponentAttacks': member.get('opponentAttacks', 0)
        })
    
    # Sort members by map position
    members.sort(key=lambda x: x['mapPosition'])
    
    return {
        'tag': clan_raw.get('tag'),
        'name': clan_raw.get('name'),
        'badge': clan_raw.get('badgeUrls', {}).get('medium', ''),
        'stars': clan_raw.get('stars', 0),
        'attacks': clan_raw.get('attacks', 0),
        'destructionPercentage': clan_raw.get('destructionPercentage', 0),
        'members': members
    }

@app.route('/api/clan/<clan_tag>')
def get_clan_info(clan_tag):
    """Get basic clan information"""
    try:
        logger.info(f"Fetching clan info for: {clan_tag}")
        
        formatted_tag = format_clan_tag(clan_tag)
        encoded_tag = requests.utils.quote(formatted_tag, safe='')
        
        clan_url = f"{COC_API_BASE}/clans/{encoded_tag}"
        response = requests.get(clan_url, headers=HEADERS, timeout=10)
        
        if response.status_code == 404:
            return jsonify({
                'error': 'clan_not_found',
                'message': 'Clan not found'
            }), 404
        
        if response.status_code != 200:
            return jsonify({
                'error': 'api_error',
                'message': f'API Error: {response.status_code}'
            }), response.status_code
        
        clan_data = response.json()
        
        return jsonify({
            'tag': clan_data.get('tag'),
            'name': clan_data.get('name'),
            'badge': clan_data.get('badgeUrls', {}).get('medium', ''),
            'level': clan_data.get('clanLevel'),
            'members': clan_data.get('members'),
            'isWarLogPublic': clan_data.get('isWarLogPublic', False)
        })
        
    except requests.RequestException as e:
        logger.error(f"Network error: {str(e)}")
        return jsonify({
            'error': 'network_error',
            'message': 'Failed to connect to Clash of Clans API'
        }), 503
        
    except Exception as e:
        logger.error(f"Server error: {str(e)}")
        return jsonify({
            'error': 'server_error',
            'message': f'Server error: {str(e)}'
        }), 500

@app.errorhandler(404)
def not_found(error):
    return jsonify({
        'error': 'not_found',
        'message': 'Endpoint not found'
    }), 404

@app.errorhandler(500)
def internal_error(error):
    return jsonify({
        'error': 'internal_error',
        'message': 'Internal server error'
    }), 500

@app.errorhandler(429)
def rate_limit_error(error):
    return jsonify({
        'error': 'rate_limit',
        'message': 'Too many requests. Please try again later.'
    }), 429

if __name__ == '__main__':
    # Check if API token is set
    if COC_API_TOKEN == 'YOUR_API_TOKEN_HERE':
        logger.warning("⚠️  WARNING: Please set your COC_API_TOKEN environment variable!")
        logger.warning("   Get your token from: https://developer.clashofclans.com/")
        logger.warning("   Set it with: export COC_API_TOKEN='your_token_here'")
    
    port = int(os.environ.get('PORT', 5000))
    debug_mode = os.environ.get('FLASK_ENV') == 'development'
    
    logger.info("🚀 ClashBerry API starting...")
    logger.info(f"📍 Server will be available at: http://0.0.0.0:{port}")
    logger.info("🔍 Make sure to set your COC_API_TOKEN environment variable")
    
    app.run(debug=debug_mode, host='0.0.0.0', port=port)