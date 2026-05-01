package com.StreetAlert.Street_Alert.controller;

import com.StreetAlert.Street_Alert.dto.Request.SubscriptionRequestDTO;
import com.StreetAlert.Street_Alert.dto.Response.SubscriptionResponseDTO;
import com.StreetAlert.Street_Alert.entity.User;
import com.StreetAlert.Street_Alert.repository.UserRepository;
import com.StreetAlert.Street_Alert.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {
	private final SubscriptionService subscriptionService;
	private final UserRepository userRepository;

	@GetMapping
	public ResponseEntity<List<SubscriptionResponseDTO>> list() {
		User user = requireCurrentUser();
		List<SubscriptionResponseDTO> response = subscriptionService.listForUser(user.getId()).stream()
				.map(SubscriptionResponseDTO::from)
				.toList();
		return ResponseEntity.ok(response);
	}

	@PostMapping
	public ResponseEntity<SubscriptionResponseDTO> create(@Valid @RequestBody SubscriptionRequestDTO request) {
		User user = requireCurrentUser();
		return ResponseEntity.ok(SubscriptionResponseDTO.from(subscriptionService.createForUser(user, request)));
	}

	@PutMapping("/{id}")
	public ResponseEntity<SubscriptionResponseDTO> update(
			@PathVariable Long id,
			@Valid @RequestBody SubscriptionRequestDTO request) {
		User user = requireCurrentUser();
		return ResponseEntity.ok(SubscriptionResponseDTO.from(subscriptionService.updateForUser(user.getId(), id, request)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		User user = requireCurrentUser();
		subscriptionService.deleteForUser(user.getId(), id);
		return ResponseEntity.noContent().build();
	}

	private User requireCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || auth.getName() == null) {
			throw new ResponseStatusException(UNAUTHORIZED, "Unauthorized");
		}
		return userRepository.findByUsername(auth.getName())
				.orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "Unauthorized"));
	}
}
