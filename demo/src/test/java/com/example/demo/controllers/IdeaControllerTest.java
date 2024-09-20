package com.example.demo.controllers;

import com.example.demo.dto.general.CompetitionDTO;
import com.example.demo.dto.input.InputCategoryDTO;
import com.example.demo.dto.input.InputIdeaDTO;
import com.example.demo.dto.output.OutputCategoryDTO;
import com.example.demo.dto.output.OutputIdeaDTO;
import com.example.demo.dto.output.OutputUserDTO;
import com.example.demo.models.Idea;
import com.example.demo.models.User;
import com.example.demo.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(IdeaController.class)
@WithMockUser(username = "user", roles = {"User"})
public class IdeaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IdeaService ideaService;

    @MockBean
    private CompetitionService competitionService;

    @MockBean
    private CommentService commentService;
    @MockBean
    private UserSelectionService userSelectionService;
    @MockBean
    private VoteTypeService voteTypeService;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private BookmarkService bookmarkService;

    @MockBean
    private AzureBlobStorageService blobService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testListIdeas() throws Exception {
        List<OutputIdeaDTO> ideas = List.of(new OutputIdeaDTO("Title", "Description", "keyFeatures", "referenceLinks", "pictures", "createdAt"));
        User currentUser = new User("username","email", "password");

        ideas.get(0).setId(1);
        currentUser.setId(1);

        OutputUserDTO currentUserDto = new OutputUserDTO(1,"username");
        ideas.get(0).setUser(currentUserDto);

        when(ideaService.getFormattedIdeas(null)).thenReturn(ideas);
        when(authenticationService.getCurrentUser()).thenReturn(currentUser);
        when(bookmarkService.getBookmarkStatusMap(any(), any())).thenReturn(Map.of(1, true));

        mockMvc.perform(MockMvcRequestBuilders.get("/ideas"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("ideas"))
                .andExpect(MockMvcResultMatchers.model().attribute("ideas", ideas))
                .andExpect(MockMvcResultMatchers.model().attribute("username", "username"))
                .andExpect(MockMvcResultMatchers.model().attribute("user_id", 1))
                .andExpect(MockMvcResultMatchers.model().attribute("bookmarkStatusMap", Map.of(1, true)))
                .andExpect(MockMvcResultMatchers.model().attribute("search", (Object) null));
    }

    @Test
    public void testAddIdea() throws Exception {
        Idea idea = new Idea();
        idea.setId(1);

        InputIdeaDTO ideaDTO = new InputIdeaDTO();
        ideaDTO.setTitle("New Idea");
        ideaDTO.setDescription("Description");
        ideaDTO.setCategories(Set.of(new InputCategoryDTO("application")));
        ideaDTO.setCompetition(new CompetitionDTO());
        ideaDTO.setUser(new OutputUserDTO("username"));

        MockMultipartFile file = new MockMultipartFile("fileUpload", "test.png", "image/png", "test".getBytes());
        when(blobService.uploadFiles(any())).thenReturn(List.of("https://blobstorage.com/test.png"));
        when(ideaService.addNewIdea(any(InputIdeaDTO.class))).thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/ideas/new-idea/create")
                        .file(file)
                        .param("title", "New Idea")
                        .param("description", "Description")
                        .param("createdAt", "2024-09-18T12:00:00")
                        .param("categories", "1")
                        .param("competition.id", "1")
                        .param("user.id", "1")
                        .with(csrf())
                )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/ideas/1"));
    }

    @Test
    public void testEditIdeaForm() throws Exception {
        User currentUser = new User("username","email", "password");

        OutputIdeaDTO idea = new OutputIdeaDTO("Title", "Description", "keyFeatures", "referenceLinks", "pictures", "createdAt");

        when(ideaService.findById(1)).thenReturn(idea);
        when(categoryService.getAllCategories()).thenReturn(List.of(new OutputCategoryDTO(1, "Category")));
        when(authenticationService.getCurrentUser()).thenReturn(currentUser);

        mockMvc.perform(MockMvcRequestBuilders.get("/ideas/1/edit"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("edit-idea"))
                .andExpect(MockMvcResultMatchers.model().attribute("ideaDTO", idea))
                .andExpect(MockMvcResultMatchers.model().attribute("allCategories", List.of(new OutputCategoryDTO(1, "Category"))));
    }

    @Test
    public void testUpdateIdea() throws Exception {
        OutputIdeaDTO updatedIdeaDTO = new OutputIdeaDTO();
        updatedIdeaDTO.setTitle("Updated Idea");
        updatedIdeaDTO.setDescription("Updated Description");
        updatedIdeaDTO.setKeyFeatures("key");
        updatedIdeaDTO.setReferenceLinks("reflinks");
        updatedIdeaDTO.setCreatedAt("2024-09-18T12:00:00");
        updatedIdeaDTO.setPictures("https://blobstorage.com/updated.png");

        MockMultipartFile file = new MockMultipartFile("fileUpload", "updated.png", "image/png", "updated".getBytes());

        when(ideaService.findById(1)).thenReturn(updatedIdeaDTO);
        when(blobService.uploadFiles(any())).thenReturn(List.of("https://blobstorage.com/updated.png"));
        doNothing().when(ideaService).updateIdea(any(), any());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/ideas/1/update")
                        .file(file)
                        .param("title", "Updated Idea")
                        .param("description", "Updated Description")
                        .param("createdAt", "2024-09-18T12:00:00")
                        .param("picturesToRemove", "")
                        .with(csrf())
                )
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/ideas/1"));

        verify(ideaService).updateIdea(eq(1), any(OutputIdeaDTO.class));
        verify(blobService).uploadFiles(any());
    }

    @Test
    public void testDeleteIdea() throws Exception {
        OutputIdeaDTO idea = new OutputIdeaDTO("Title", "Description", "keyFeatures", "reference", "http://blobstorage.com/test.png", "createdAt");
        idea.setId(1);

        when(ideaService.findById(1)).thenReturn(idea);
        doNothing().when(blobService).deleteBlob(any());
        doNothing().when(ideaService).deleteIdea(1);

        mockMvc.perform(MockMvcRequestBuilders.post("/ideas/1/delete")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/ideas"));
    }
}
