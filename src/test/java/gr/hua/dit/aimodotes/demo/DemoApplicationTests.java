package gr.hua.dit.aimodotes.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DemoApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@WithMockUser(roles = "ADMIN")
	@Test
	public void testCreateUser() throws Exception {
		// Arrange
		String userJson = "{\"username\":\"apiuser\",\"email\":\"api@hua.gr\",\"password\":\"1234\"}";


		// Act
		ResultActions result = mockMvc.perform(post("/api/admin/user/new")
				.contentType(MediaType.APPLICATION_JSON)
				.content(userJson));

		// Assert
		result.andExpect(status().isOk());
	}

	@Test
	public void testSignUser() throws Exception {
		// Arrange
		String userJson = "{\"username\":\"apiuser\",\"password\":\"1234\"}";

		// Act
		ResultActions result = mockMvc.perform(post("/api/auth/signin")
				.contentType(MediaType.APPLICATION_JSON)
				.content(userJson));

		// Assert
		result.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("apiuser"));

	}
}
