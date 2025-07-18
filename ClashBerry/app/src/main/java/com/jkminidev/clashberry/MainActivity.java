package com.jkminidev.clashberry;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayoutMediator;
import com.jkminidev.clashberry.adapters.WarStatsAdapter;
import com.jkminidev.clashberry.adapters.WarTabsAdapter;
import com.jkminidev.clashberry.api.ApiClient;
import com.jkminidev.clashberry.api.ClashApiService;
import com.jkminidev.clashberry.databinding.ActivityMainBinding;
import com.jkminidev.clashberry.models.WarData;
import com.jkminidev.clashberry.models.ErrorResponse;
import com.jkminidev.clashberry.models.WarStat;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ClashApiService apiService;
    private WarStatsAdapter warStatsAdapter;
    private WarTabsAdapter warTabsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeApiService();
        setupViews();
        setupClickListeners();
    }

    private void initializeApiService() {
        apiService = ApiClient.getClashApiService();
    }

    private void setupViews() {
        // Setup war stats recycler view
        warStatsAdapter = new WarStatsAdapter();
        binding.warStatsRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        binding.warStatsRecycler.setAdapter(warStatsAdapter);
    }

    private void setupClickListeners() {
        binding.searchButton.setOnClickListener(v -> searchWar());
        
        // Handle enter key in search input
        binding.clanTagInput.setOnEditorActionListener((v, actionId, event) -> {
            searchWar();
            return true;
        });
    }

    private void searchWar() {
        String clanTag = binding.clanTagInput.getText().toString().trim();
        
        if (TextUtils.isEmpty(clanTag)) {
            Toast.makeText(this, R.string.please_enter_clan_tag, Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading(true);
        hideResults();
        hideError();

        // Format clan tag to ensure it starts with #
        if (!clanTag.startsWith("#")) {
            clanTag = "#" + clanTag;
        }

        Call<WarData> call = apiService.getWarData(clanTag);
        call.enqueue(new Callback<WarData>() {
            @Override
            public void onResponse(Call<WarData> call, Response<WarData> response) {
                showLoading(false);
                
                if (response.isSuccessful() && response.body() != null) {
                    showWarResults(response.body());
                } else {
                    // Try to parse error response
                    try {
                        ErrorResponse errorResponse = ApiClient.parseErrorResponse(response);
                        showError(errorResponse);
                    } catch (Exception e) {
                        showError(new ErrorResponse("api_error", 
                            getString(R.string.api_error_format, response.code()), null));
                    }
                }
            }

            @Override
            public void onFailure(Call<WarData> call, Throwable t) {
                showLoading(false);
                showError(new ErrorResponse("network_error", 
                    getString(R.string.network_error), null));
            }
        });
    }

    private void showLoading(boolean show) {
        binding.loadingSection.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.searchButton.setEnabled(!show);
    }

    private void showWarResults(WarData warData) {
        binding.resultsSection.setVisibility(View.VISIBLE);
        
        // Set clan information
        binding.clanName.setText(warData.getClan().getName());
        binding.clanTag.setText(warData.getClan().getTag());
        
        // Load clan badge
        Glide.with(this)
            .load(warData.getClan().getBadge())
            .placeholder(R.mipmap.ic_launcher)
            .into(binding.clanBadge);
        
        // Set opponent information
        binding.opponentName.setText(warData.getOpponent().getName());
        binding.opponentTag.setText(warData.getOpponent().getTag());
        
        // Load opponent badge
        Glide.with(this)
            .load(warData.getOpponent().getBadge())
            .placeholder(R.mipmap.ic_launcher)
            .into(binding.opponentBadge);
        
        // Set war status
        setWarStatus(warData);
        
        // Set war stats
        setupWarStats(warData);
        
        // Setup tabs
        setupTabs(warData);
    }

    private void setWarStatus(WarData warData) {
        String statusText = getWarStatusText(warData.getState());
        
        // Add time remaining if available
        if (warData.getTimeRemaining() != null && warData.getTimeLabel() != null) {
            statusText += " • " + warData.getTimeRemaining() + " " + warData.getTimeLabel();
        }
        
        // Add CWL round if applicable
        if ("cwl".equals(warData.getWarType()) && warData.getCwlRound() != null) {
            statusText += " • " + getString(R.string.cwl_round_format, warData.getCwlRound());
        }
        
        binding.warStatus.setText(statusText);
        
        // Set status color based on war state
        int statusColor = getWarStatusColor(warData.getState());
        binding.warStatus.setTextColor(statusColor);
    }

    private String getWarStatusText(String state) {
        switch (state) {
            case "preparation":
                return getString(R.string.preparation);
            case "inWar":
                return getString(R.string.battle_day);
            case "warEnded":
                return getString(R.string.war_ended);
            default:
                return getString(R.string.unknown);
        }
    }

    private int getWarStatusColor(String state) {
        switch (state) {
            case "preparation":
                return getColor(R.color.status_preparation);
            case "inWar":
                return getColor(R.color.status_in_war);
            case "warEnded":
                return getColor(R.color.status_war_ended);
            default:
                return getColor(R.color.text_color);
        }
    }

    private void setupWarStats(WarData warData) {
        List<WarStat> stats = new ArrayList<>();
        
        int maxAttacks = warData.getTeamSize() * ("cwl".equals(warData.getWarType()) ? 1 : 2);
        int maxStars = warData.getTeamSize() * 3;
        
        // Clan stats
        stats.add(new WarStat(
            warData.getClan().getStars() + "/" + maxStars,
            warData.getClan().getName() + " " + getString(R.string.stars)
        ));
        stats.add(new WarStat(
            String.format("%.2f%%", warData.getClan().getDestructionPercentage()),
            warData.getClan().getName() + " " + getString(R.string.destruction)
        ));
        stats.add(new WarStat(
            warData.getClan().getAttacks() + "/" + maxAttacks,
            warData.getClan().getName() + " " + getString(R.string.attacks)
        ));
        
        // Opponent stats
        stats.add(new WarStat(
            warData.getOpponent().getStars() + "/" + maxStars,
            warData.getOpponent().getName() + " " + getString(R.string.stars)
        ));
        stats.add(new WarStat(
            String.format("%.2f%%", warData.getOpponent().getDestructionPercentage()),
            warData.getOpponent().getName() + " " + getString(R.string.destruction)
        ));
        stats.add(new WarStat(
            warData.getOpponent().getAttacks() + "/" + maxAttacks,
            warData.getOpponent().getName() + " " + getString(R.string.attacks)
        ));
        
        warStatsAdapter.updateStats(stats);
    }

    private void setupTabs(WarData warData) {
        warTabsAdapter = new WarTabsAdapter(this, warData);
        binding.viewPager.setAdapter(warTabsAdapter);
        
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText(R.string.overview);
                    break;
                case 1:
                    tab.setText(R.string.attacks_tab);
                    break;
                case 2:
                    tab.setText(R.string.defenses);
                    break;
                case 3:
                    tab.setText(R.string.roster);
                    break;
            }
        }).attach();
    }

    private void hideResults() {
        binding.resultsSection.setVisibility(View.GONE);
    }

    private void showError(ErrorResponse errorResponse) {
        binding.errorCard.setVisibility(View.VISIBLE);
        
        String title = getErrorTitle(errorResponse.getError());
        String message = getErrorMessage(errorResponse);
        
        binding.errorTitle.setText(title);
        binding.errorMessage.setText(message);
    }

    private void hideError() {
        binding.errorCard.setVisibility(View.GONE);
    }

    private String getErrorTitle(String errorType) {
        switch (errorType) {
            case "private_war_log":
                return getString(R.string.private_war_log);
            case "not_in_war":
                return getString(R.string.not_in_war);
            case "clan_not_found":
                return getString(R.string.error);
            default:
                return getString(R.string.error);
        }
    }

    private String getErrorMessage(ErrorResponse errorResponse) {
        switch (errorResponse.getError()) {
            case "private_war_log":
                return getString(R.string.private_war_log_message);
            case "not_in_war":
                return getString(R.string.not_in_war_message);
            case "clan_not_found":
                return getString(R.string.clan_not_found);
            case "network_error":
                return getString(R.string.network_error);
            default:
                return errorResponse.getMessage();
        }
    }
}