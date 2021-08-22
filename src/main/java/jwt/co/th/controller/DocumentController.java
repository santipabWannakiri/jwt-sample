package jwt.co.th.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping()
public class DocumentController {

	@GetMapping("/")
	@ResponseBody
	public String anyone() {

		return "anyone can access ";
	}
	
	
	@GetMapping("/admin")
	@ResponseBody
	public String admin() {

		return "This can access by admin only";
	}
	
	@GetMapping("/user")
	@ResponseBody
	public String user() {

		return "This can access by user only";
	}
	
	
	@GetMapping("/403")
	@ResponseBody
	public String accessDenied() {

		return "errors/403";
	}
	
}
