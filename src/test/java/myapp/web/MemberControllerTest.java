package myapp.web;

import myapp.dao.MemberDAO;
import myapp.model.Member;
import myapp.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberApiController.class)
@SuppressWarnings("removal")
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberDAO memberDAO;

    @MockBean
    @SuppressWarnings("unused")
    private UserDetailsServiceImpl userDetailsService;

    @Test
    @WithMockUser
    public void testListMembers() throws Exception {
        Member member = new Member();
        member.setLastName("Doe");
        member.setFirstName("John");
        member.setEmail("john.doe@example.com");

        when(memberDAO.getAllMembers()).thenReturn(Collections.singletonList(member));

        mockMvc.perform(get("/api/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lastName").value("Doe"));
    }
}
