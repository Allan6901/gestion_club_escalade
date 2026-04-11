package myapp.web;

import myapp.model.Member;
import myapp.repo.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberController.class)
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberRepository memberRepository;

    @Test
    @WithMockUser
    public void testListMembers() throws Exception {
        Member member = new Member();
        member.setLastName("Doe");
        member.setFirstName("John");
        member.setEmail("john.doe@example.com");

        when(memberRepository.findAll()).thenReturn(Arrays.asList(member));

        mockMvc.perform(get("/members"))
                .andExpect(status().isOk())
                .andExpect(view().name("members"))
                .andExpect(model().attributeExists("members"));
    }

    @Test
    @WithMockUser
    public void testAddMember() throws Exception {
        mockMvc.perform(post("/members")
                .param("lastName", "Doe")
                .param("firstName", "John")
                .param("email", "john.doe@example.com")
                .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/members"));
    }

}
