package com.jkminidev.clashberry

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.jkminidev.clashberry.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private val apiService = ApiService()
    
    // This will be set when hosting the backend - placeholder for now
    private val API_BASE_URL = "https://your-backend-url.com"
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupStatusBar()
        setupUI()
        setupWebView()
    }
    
    private fun setupStatusBar() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, binding.root)
        controller.hide(WindowInsetsCompat.Type.statusBars())
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
    
    private fun setupUI() {
        binding.searchButton.setOnClickListener {
            searchWar()
        }
        
        binding.retryButton.setOnClickListener {
            searchWar()
        }
        
        binding.clanTagInput.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || 
                (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                searchWar()
                true
            } else {
                false
            }
        }
        
        binding.swipeRefreshLayout.setOnRefreshListener {
            searchWar()
        }
        
        binding.swipeRefreshLayout.setColorSchemeResources(
            R.color.accent_green,
            R.color.accent_green_dark
        )
    }
    
    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        with(binding.webView) {
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                cacheMode = WebSettings.LOAD_DEFAULT
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                userAgentString = "ClashBerry Android App"
                allowFileAccess = false
                allowContentAccess = false
                setGeolocationEnabled(false)
            }
            
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    // Keep navigation within the app
                    return false
                }
                
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }
            
            addJavascriptInterface(WebAppInterface(), "Android")
        }
    }
    
    private fun searchWar() {
        val clanTag = binding.clanTagInput.text?.toString()?.trim()
        
        if (clanTag.isNullOrEmpty()) {
            Toast.makeText(this, "Please enter a clan tag", Toast.LENGTH_SHORT).show()
            return
        }
        
        showLoading(true)
        
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val warData = withContext(Dispatchers.IO) {
                    apiService.getWarData(API_BASE_URL, clanTag)
                }
                
                if (warData != null) {
                    showWebView(warData, clanTag)
                } else {
                    showError("Failed to load war data", "Please check the clan tag and try again.")
                }
                
            } catch (e: Exception) {
                showError("Network Error", "Failed to connect to server. Please try again.")
            } finally {
                showLoading(false)
            }
        }
    }
    
    private fun showLoading(show: Boolean) {
        binding.loadingLayout.visibility = if (show) View.VISIBLE else View.GONE
        binding.errorLayout.visibility = View.GONE
        binding.swipeRefreshLayout.visibility = View.GONE
        binding.searchButton.isEnabled = !show
        
        if (!show) {
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }
    
    private fun showError(title: String, message: String) {
        binding.loadingLayout.visibility = View.GONE
        binding.swipeRefreshLayout.visibility = View.GONE
        binding.errorLayout.visibility = View.VISIBLE
        
        binding.errorTitle.text = title
        binding.errorMessage.text = message
    }
    
    private fun showWebView(warData: String, clanTag: String) {
        binding.loadingLayout.visibility = View.GONE
        binding.errorLayout.visibility = View.GONE
        binding.swipeRefreshLayout.visibility = View.VISIBLE
        
        val html = generateWarHTML(warData, clanTag)
        binding.webView.loadDataWithBaseURL(
            API_BASE_URL, 
            html, 
            "text/html", 
            "UTF-8", 
            null
        )
    }
    
    private fun generateWarHTML(warData: String, clanTag: String): String {
        return """
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>ClashBerry - War Results</title>
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" />
            <style>
                @import url("https://fonts.googleapis.com/css?family=Raleway:100,200,300,400,500,600,700,800,900");
                
                :root {
                    --bg-primary: #000000;
                    --text-color: #ffffff;
                    --weight-small: 400;
                    --weight-semibold: 600;
                    --weight-bold: 800;
                    --accent-color: #00ff88;
                    --error-color: #ff4757;
                    --warning-color: #EB5406;
                }
                
                * {
                    margin: 0;
                    padding: 0;
                    box-sizing: border-box;
                }
                
                body {
                    background: var(--bg-primary);
                    color: var(--text-color);
                    font-family: "Raleway", sans-serif;
                    padding: 16px;
                    line-height: 1.6;
                }
                
                .war-card {
                    background: rgba(255, 255, 255, 0.05);
                    border: 1px solid rgba(255, 255, 255, 0.1);
                    border-radius: 16px;
                    padding: 1.5rem;
                    margin-bottom: 1rem;
                    transition: all 0.3s ease;
                }
                
                .war-header {
                    display: flex;
                    align-items: center;
                    justify-content: space-between;
                    margin-bottom: 1.5rem;
                    flex-wrap: wrap;
                    gap: 1rem;
                }
                
                .clan-info {
                    display: flex;
                    align-items: center;
                    gap: 1rem;
                }
                
                .clan-badge {
                    width: 50px;
                    height: 50px;
                    border-radius: 50%;
                    border: 2px solid rgba(255, 255, 255, 0.2);
                }
                
                .clan-details h3 {
                    font-size: 1.3rem;
                    margin-bottom: 0.25rem;
                }
                
                .clan-tag {
                    color: var(--accent-color);
                    font-size: 0.9rem;
                }
                
                .war-stats {
                    text-align: center;
                }
                
                .score {
                    font-size: 2rem;
                    font-weight: var(--weight-bold);
                    color: var(--accent-color);
                }
                
                .war-status {
                    margin: 1rem 0;
                    padding: 1rem;
                    background: rgba(0, 255, 136, 0.1);
                    border: 1px solid var(--accent-color);
                    border-radius: 12px;
                    text-align: center;
                }
                
                .status-badge {
                    display: inline-block;
                    padding: 0.5rem 1rem;
                    border-radius: 25px;
                    font-weight: var(--weight-bold);
                    margin-bottom: 0.5rem;
                }
                
                .status-preparation {
                    background: linear-gradient(135deg, #ffd700, #ffed4e);
                    color: #000;
                }
                
                .status-war {
                    background: linear-gradient(135deg, #ff4757, #ff6b7a);
                    color: #fff;
                }
                
                .status-ended {
                    background: linear-gradient(135deg, #2f3542, #57606f);
                    color: #fff;
                }
                
                .tabs {
                    display: flex;
                    margin: 1rem 0;
                    border-bottom: 2px solid rgba(255, 255, 255, 0.1);
                }
                
                .tab {
                    flex: 1;
                    padding: 1rem;
                    text-align: center;
                    cursor: pointer;
                    border-bottom: 2px solid transparent;
                    transition: all 0.3s ease;
                    font-weight: var(--weight-semibold);
                }
                
                .tab.active {
                    border-bottom-color: var(--accent-color);
                    color: var(--accent-color);
                }
                
                .tab-content {
                    display: none;
                    margin-top: 1rem;
                }
                
                .tab-content.active {
                    display: block;
                }
                
                .members-grid {
                    display: grid;
                    grid-template-columns: 1fr;
                    gap: 1rem;
                    margin-top: 1rem;
                }
                
                .member-card {
                    background: rgba(255, 255, 255, 0.03);
                    border: 1px solid rgba(255, 255, 255, 0.1);
                    border-radius: 12px;
                    padding: 1rem;
                    display: flex;
                    justify-content: space-between;
                    align-items: center;
                    gap: 1rem;
                }
                
                .member-info {
                    display: flex;
                    align-items: center;
                    gap: 0.75rem;
                    flex: 1;
                }
                
                .th-emoji {
                    font-size: 1.5rem;
                }
                
                .member-name {
                    font-weight: var(--weight-semibold);
                    font-size: 1rem;
                }
                
                .member-tag {
                    color: var(--accent-color);
                    font-size: 0.8rem;
                }
                
                .attacks-info {
                    text-align: right;
                    min-width: 100px;
                }
                
                .attack-item {
                    margin-bottom: 0.25rem;
                    font-size: 0.9rem;
                }
                
                .stars {
                    color: #ffd700;
                    font-size: 1rem;
                }
                
                .th-breakdown {
                    display: flex;
                    flex-wrap: wrap;
                    gap: 0.5rem;
                    margin: 1rem 0;
                    justify-content: center;
                }
                
                .th-item {
                    background: rgba(255, 255, 255, 0.1);
                    border-radius: 8px;
                    padding: 0.5rem 1rem;
                    display: flex;
                    align-items: center;
                    gap: 0.5rem;
                    font-size: 0.9rem;
                }
                
                .no-results {
                    text-align: center;
                    padding: 2rem;
                    color: var(--text-secondary);
                }
                
                .no-results i {
                    font-size: 3rem;
                    margin-bottom: 1rem;
                    opacity: 0.5;
                }
                
                @media (max-width: 768px) {
                    .war-header {
                        flex-direction: column;
                        text-align: center;
                    }
                    
                    .tabs {
                        flex-direction: column;
                    }
                    
                    .member-card {
                        flex-direction: column;
                        text-align: center;
                    }
                    
                    .attacks-info {
                        text-align: center;
                    }
                }
            </style>
        </head>
        <body>
            <div id="warResults"></div>
            
            <script>
                const warData = $warData;
                
                function showWarResults(data) {
                    const container = document.getElementById('warResults');
                    container.innerHTML = createWarCard(data);
                    setupTabs();
                }
                
                function createWarCard(warData) {
                    const statusClass = warData.state === 'preparation' ? 'status-preparation' : 
                                      warData.state === 'inWar' ? 'status-war' : 'status-ended';
                    const statusText = warData.state === 'preparation' ? 'Preparation Day' : 
                                     warData.state === 'inWar' ? 'War Day' : 'War Ended';
                    
                    return `
                        <div class="war-card">
                            <div class="war-header">
                                <div class="clan-info">
                                    <img src="${"${warData.clan.badge}"}" alt="Clan Badge" class="clan-badge" onerror="this.style.display='none'">
                                    <div class="clan-details">
                                        <h3>${"${warData.clan.name}"}</h3>
                                        <div class="clan-tag">${"${warData.clan.tag}"}</div>
                                    </div>
                                </div>
                                <div class="war-stats">
                                    <div class="score">${"${warData.clan.stars}"}</div>
                                    <div>vs</div>
                                    <div class="score">${"${warData.opponent.stars}"}</div>
                                </div>
                                <div class="clan-info">
                                    <div class="clan-details" style="text-align: right;">
                                        <h3>${"${warData.opponent.name}"}</h3>
                                        <div class="clan-tag">${"${warData.opponent.tag}"}</div>
                                    </div>
                                    <img src="${"${warData.opponent.badge}"}" alt="Opponent Badge" class="clan-badge" onerror="this.style.display='none'">
                                </div>
                            </div>
                            
                            <div class="war-status">
                                <div class="status-badge ${"${statusClass}"}">
                                    ${"${statusText}"}
                                </div>
                                ${"${warData.timeRemaining ? `<div>Time remaining: ${"${warData.timeRemaining}"}</div>` : ''}"}
                            </div>
                            
                            <div class="tabs">
                                <div class="tab active" data-tab="attacks">Attacks</div>
                                <div class="tab" data-tab="defenses">Defenses</div>
                                <div class="tab" data-tab="roster">Roster</div>
                            </div>
                            
                            <div id="attacks" class="tab-content active">
                                ${"${createAttacksTab(warData)}"}
                            </div>
                            
                            <div id="defenses" class="tab-content">
                                ${"${createDefensesTab(warData)}"}
                            </div>
                            
                            <div id="roster" class="tab-content">
                                ${"${createRosterTab(warData)}"}
                            </div>
                        </div>
                    `;
                }
                
                function createAttacksTab(warData) {
                    const attacksExpected = warData.warType === 'cwl' ? 1 : 2;
                    let attacksHTML = `
                        <h3>Attacks (${"${warData.clan.attacks}"}/${"${warData.teamSize * attacksExpected}"})</h3>
                        <div class="members-grid">
                    `;
                    
                    warData.clan.members.forEach(member => {
                        attacksHTML += `
                            <div class="member-card">
                                <div class="member-info">
                                    <span class="th-emoji">${"${member.thEmoji}"}</span>
                                    <div>
                                        <div class="member-name">${"${member.name}"}</div>
                                        <div class="member-tag">${"${member.tag}"}</div>
                                    </div>
                                </div>
                                <div class="attacks-info">
                                    ${"${member.attacks.length > 0 ? 
                                        member.attacks.map(attack => `
                                            <div class="attack-item">
                                                <span class="stars">${"${'★'.repeat(attack.stars)}${'☆'.repeat(3 - attack.stars)}"}</span>
                                                <span>${"${attack.destructionPercentage}"}%</span>
                                            </div>
                                        `).join('') : 
                                        '<div class="attack-item">No attacks yet</div>'
                                    }"}
                                </div>
                            </div>
                        `;
                    });
                    
                    attacksHTML += `</div>`;
                    return attacksHTML;
                }
                
                function createDefensesTab(warData) {
                    const allMembers = [...warData.clan.members, ...warData.opponent.members];
                    const memberMap = {};
                    allMembers.forEach(member => {
                        memberMap[member.tag] = member;
                    });
                    
                    const defenses = [];
                    warData.opponent.members.forEach(opponentMember => {
                        opponentMember.attacks.forEach(attack => {
                            if (memberMap[attack.defenderTag] && warData.clan.members.some(m => m.tag === attack.defenderTag)) {
                                defenses.push({
                                    attacker: opponentMember,
                                    defender: memberMap[attack.defenderTag],
                                    stars: attack.stars,
                                    destruction: attack.destructionPercentage
                                });
                            }
                        });
                    });
                    
                    let defensesHTML = `<h3>Defenses (${"${defenses.length}"}/${"${warData.teamSize * (warData.warType === 'cwl' ? 1 : 2)}"})</h3>`;
                    
                    if (defenses.length > 0) {
                        defensesHTML += `<div class="members-grid">`;
                        defenses.forEach(defense => {
                            defensesHTML += `
                                <div class="member-card">
                                    <div class="member-info">
                                        <span class="th-emoji">${"${defense.defender.thEmoji}"}</span>
                                        <div>
                                            <div class="member-name">${"${defense.defender.name}"}</div>
                                            <div class="member-tag">${"${defense.defender.tag}"}</div>
                                        </div>
                                    </div>
                                    <div class="attacks-info">
                                        <div class="attack-item">
                                            <span>Defended by:</span>
                                            <span class="th-emoji">${"${defense.attacker.thEmoji}"}</span>
                                            <span>${"${defense.attacker.name}"}</span>
                                        </div>
                                        <div class="attack-item">
                                            <span class="stars">${"${'★'.repeat(defense.stars)}${'☆'.repeat(3 - defense.stars)}"}</span>
                                            <span>${"${defense.destruction}"}%</span>
                                        </div>
                                    </div>
                                </div>
                            `;
                        });
                        defensesHTML += `</div>`;
                    } else {
                        defensesHTML += `
                            <div class="no-results">
                                <i class="fas fa-shield-alt"></i>
                                <p>No defenses recorded yet</p>
                            </div>
                        `;
                    }
                    
                    return defensesHTML;
                }
                
                function createRosterTab(warData) {
                    return `
                        <div style="margin-bottom: 2rem;">
                            <h3>${"${warData.clan.name}"} Roster</h3>
                            <div class="members-grid">
                                ${"${warData.clan.members.map(member => `
                                    <div class="member-card">
                                        <div class="member-info">
                                            <span class="th-emoji">${"${member.thEmoji}"}</span>
                                            <div>
                                                <div class="member-name">${"${member.name}"}</div>
                                                <div class="member-tag">${"${member.tag}"}</div>
                                            </div>
                                        </div>
                                    </div>
                                `).join('')}"}
                            </div>
                        </div>
                        <div>
                            <h3>${"${warData.opponent.name}"} Roster</h3>
                            <div class="members-grid">
                                ${"${warData.opponent.members.map(member => `
                                    <div class="member-card">
                                        <div class="member-info">
                                            <span class="th-emoji">${"${member.thEmoji}"}</span>
                                            <div>
                                                <div class="member-name">${"${member.name}"}</div>
                                                <div class="member-tag">${"${member.tag}"}</div>
                                            </div>
                                        </div>
                                    </div>
                                `).join('')}"}
                            </div>
                        </div>
                    `;
                }
                
                function setupTabs() {
                    const tabs = document.querySelectorAll('.tab');
                    const contents = document.querySelectorAll('.tab-content');
                    
                    tabs.forEach(tab => {
                        tab.addEventListener('click', () => {
                            const targetTab = tab.dataset.tab;
                            
                            tabs.forEach(t => t.classList.remove('active'));
                            contents.forEach(c => c.classList.remove('active'));
                            
                            tab.classList.add('active');
                            document.getElementById(targetTab).classList.add('active');
                        });
                    });
                }
                
                // Initialize
                showWarResults(warData);
            </script>
        </body>
        </html>
        """.trimIndent()
    }
    
    inner class WebAppInterface {
        @JavascriptInterface
        fun showToast(message: String) {
            runOnUiThread {
                Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

class ApiService {
    suspend fun getWarData(baseUrl: String, clanTag: String): String? {
        return try {
            val url = URL("$baseUrl/api/war/${clanTag.replace("#", "")}")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Accept", "application/json")
            connection.connectTimeout = 10000
            connection.readTimeout = 10000
            
            if (connection.responseCode == 200) {
                connection.inputStream.bufferedReader().readText()
            } else {
                null
            }
        } catch (e: IOException) {
            null
        }
    }
}