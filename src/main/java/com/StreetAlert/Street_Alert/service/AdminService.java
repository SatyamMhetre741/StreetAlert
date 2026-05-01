package com.StreetAlert.Street_Alert.service;

import com.StreetAlert.Street_Alert.dto.Request.KeywordRuleRequestDTO;
import com.StreetAlert.Street_Alert.entity.FetchLogs;
import com.StreetAlert.Street_Alert.entity.KeywordRule;
import com.StreetAlert.Street_Alert.repository.FetchLogRepository;
import com.StreetAlert.Street_Alert.repository.KeywordRuleRepository;
import com.StreetAlert.Street_Alert.scheduler.NewsFetcherScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
	private final FetchLogRepository fetchLogRepository;
	private final KeywordRuleRepository keywordRuleRepository;
	private final NewsFetcherScheduler newsFetcherScheduler;

	public void triggerFetch() {
		newsFetcherScheduler.fetchAndStore();
	}

	public List<FetchLogs> listFetchLogs() {
		return fetchLogRepository.findAllByOrderByFetchedAtDesc();
	}

	public List<KeywordRule> listKeywordRules() {
		return keywordRuleRepository.findAllByOrderByCreatedAtDesc();
	}

	public KeywordRule createKeywordRule(KeywordRuleRequestDTO request) {
		KeywordRule rule = KeywordRule.builder()
				.sector(request.getSector())
				.keyword(request.getKeyword())
				.impactScore(request.getImpactScore())
				.build();

		return keywordRuleRepository.save(rule);
	}

	public KeywordRule updateKeywordRule(Long id, KeywordRuleRequestDTO request) {
		KeywordRule rule = keywordRuleRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Keyword rule not found"));

		if (request.getSector() != null) {
			rule.setSector(request.getSector());
		}
		if (request.getKeyword() != null && !request.getKeyword().isBlank()) {
			rule.setKeyword(request.getKeyword());
		}
		if (request.getImpactScore() != null) {
			rule.setImpactScore(request.getImpactScore());
		}

		return keywordRuleRepository.save(rule);
	}

	public void deleteKeywordRule(Long id) {
		keywordRuleRepository.deleteById(id);
	}
}
