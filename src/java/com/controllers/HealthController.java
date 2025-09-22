package com.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Lightweight health endpoint for Azure App Service Health Check.
 * Returns HTTP 200 and a simple body when the app is responsive.
 */
@RestController
public class HealthController {

	@GetMapping({"/health", "/healthz", "/ready", "/health.htm"})
	public String health() {
		return "OK";
	}
}


