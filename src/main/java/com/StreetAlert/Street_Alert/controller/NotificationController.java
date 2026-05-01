package com.StreetAlert.Street_Alert.controller;

import com.StreetAlert.Street_Alert.dto.Response.NotificationResponseDTO;
import com.StreetAlert.Street_Alert.entity.User;
import com.StreetAlert.Street_Alert.repository.UserRepository;
import com.StreetAlert.Street_Alert.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
	private final NotificationService notificationService;
	private final UserRepository userRepository;

	@GetMapping
	public ResponseEntity<List<NotificationResponseDTO>> list() {
		User user = requireCurrentUser();
		List<NotificationResponseDTO> response = notificationService.listForUser(user.getId()).stream()
				.map(NotificationResponseDTO::from)
				.toList();
		return ResponseEntity.ok(response);
	}

	@GetMapping("/unread-count")
	public ResponseEntity<Map<String, Long>> unreadCount() {
		User user = requireCurrentUser();
		long count = notificationService.unreadCount(user.getId());
		return ResponseEntity.ok(Map.of("count", count));
	}

	@PatchMapping("/{id}/read")
	public ResponseEntity<Void> markRead(@PathVariable Long id) {
		User user = requireCurrentUser();
		notificationService.markAsRead(user.getId(), id);
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
