package org.se.reins.se3353_21_clusterone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@CrossOrigin(maxAge = 3600)
@RestController
public class GreetingController {

	@Autowired
	private HttpSession hs;

	@GetMapping("/about")
	public String getAbout() {
		return "This is a Spring security sample";
	}

	@GetMapping("/users")
	public String getUser() {
		return "I am a user";
	}

	@GetMapping("/")
	public String getHome() {
		if (hs.getAttribute("token") == null){
			hs.setAttribute("token", "New");
			System.out.println("token is null");
		}
		else {
			System.out.println("token is NOT null");
			hs.setAttribute("token", "Old");
		}
		return "I am 8080! Let' start! Server One: " + hs.getAttribute("token") + " " + hs.getId();
	}

}
