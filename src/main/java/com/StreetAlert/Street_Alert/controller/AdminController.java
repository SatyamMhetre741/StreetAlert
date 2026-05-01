package com.StreetAlert.Street_Alert.controller;

import com.StreetAlert.Street_Alert.dto.Request.KeywordRuleRequestDTO;
import com.StreetAlert.Street_Alert.dto.Response.FetchLogResponseDTO;
import com.StreetAlert.Street_Alert.dto.Response.KeywordRuleResponseDTO;
import com.StreetAlert.Street_Alert.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
	private final AdminService adminService;

	@PostMapping("/news/fetch")
	public ResponseEntity<Void> triggerFetch() {
		adminService.triggerFetch();
		return ResponseEntity.accepted().build();
	}

	@GetMapping("/news/fetch-logs")
	public ResponseEntity<List<FetchLogResponseDTO>> fetchLogs() {
		List<FetchLogResponseDTO> response = adminService.listFetchLogs().stream()
				.map(FetchLogResponseDTO::from)
				.toList();
		return ResponseEntity.ok(response);
	}

	@GetMapping("/keyword-rules")
	public ResponseEntity<List<KeywordRuleResponseDTO>> listKeywordRules() {
		List<KeywordRuleResponseDTO> response = adminService.listKeywordRules().stream()
				.map(KeywordRuleResponseDTO::from)
				.toList();
		return ResponseEntity.ok(response);
	}

	@PostMapping("/keyword-rules")
	public ResponseEntity<KeywordRuleResponseDTO> createKeywordRule(@Valid @RequestBody KeywordRuleRequestDTO request) {
		return ResponseEntity.ok(KeywordRuleResponseDTO.from(adminService.createKeywordRule(request)));
	}

	@PutMapping("/keyword-rules/{id}")
	public ResponseEntity<KeywordRuleResponseDTO> updateKeywordRule(
			@PathVariable Long id,
			@Valid @RequestBody KeywordRuleRequestDTO request) {
		return ResponseEntity.ok(KeywordRuleResponseDTO.from(adminService.updateKeywordRule(id, request)));
	}

	@DeleteMapping("/keyword-rules/{id}")
	public ResponseEntity<Void> deleteKeywordRule(@PathVariable Long id) {
		adminService.deleteKeywordRule(id);
		return ResponseEntity.noContent().build();
	}
}
