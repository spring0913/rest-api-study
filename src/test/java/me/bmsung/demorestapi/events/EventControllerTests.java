package me.bmsung.demorestapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.bmsung.demorestapi.common.RestDocsConfiguration;
import me.bmsung.demorestapi.common.TestDescription;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
public class EventControllerTests {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    ModelMapper modelMapper;

    @Test
    @TestDescription("??????????????? ???????????? ???????????? ?????????")
    public void createEvent() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("Study REST API with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2021, 6, 13, 18, 00))
                .closeEnrollmentDateTime(LocalDateTime.of(2021, 6, 13, 18, 00))
                .beginEventDateTime(LocalDateTime.of(2021, 6, 13, 18, 00))
                .endEventDateTime(LocalDateTime.of(2021, 6, 13, 18, 00))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("???????????????")
                .build();

        mockMvc.perform(post("/api/events")
                    .contentType(APPLICATION_JSON)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isCreated()) // 201 Created
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-event").exists())
                .andDo(document("create-event",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query events"),
                                linkWithRel("update-event").description("link to update an existing event"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("Accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-Type header")
                        ),
                        requestFields(
                                fieldWithPath("name").description("name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin enrollment new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of close enrollment new event"),
                                fieldWithPath("beginEventDateTime").description("date time of begin new event"),
                                fieldWithPath("endEventDateTime").description("date time of end new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("base price of new event"),
                                fieldWithPath("maxPrice").description("max price of new event"),
                                fieldWithPath("limitOfEnrollment").description("limit of enrollment")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-Type header")
                        ),
                        responseFields(
                                fieldWithPath("id").description("identifier of new event"),
                                fieldWithPath("name").description("name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin enrollment new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of close enrollment new event"),
                                fieldWithPath("beginEventDateTime").description("date time of begin new event"),
                                fieldWithPath("endEventDateTime").description("date time of end new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("base price of new event"),
                                fieldWithPath("maxPrice").description("max price of new event"),
                                fieldWithPath("limitOfEnrollment").description("limit of enrollment"),
                                fieldWithPath("free").description("it tells whether this event is free or not"),
                                fieldWithPath("offline").description("it tells whether this event is offline event or not"),
                                fieldWithPath("eventStatus").description("event status"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.query-events.href").description("link to query event list"),
                                fieldWithPath("_links.update-event.href").description("link to update existing event"),
                                fieldWithPath("_links.profile.href").description("link to profile")
                        )
                ));
    }

    @Test
    @TestDescription("?????? ?????? ??? ?????? ?????? ????????? ????????? ????????? ???????????? ?????????")
    public void createEvent_Bad_Request() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("Study REST API with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2021, 6, 21, 18, 00))
                .closeEnrollmentDateTime(LocalDateTime.of(2021, 6, 21, 18, 00))
                .beginEventDateTime(LocalDateTime.of(2021, 6, 21, 18, 00))
                .endEventDateTime(LocalDateTime.of(2021, 6, 21, 18, 00))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("???????????????")
                .free(true)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        mockMvc.perform(post("/api/events")
                .contentType(APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest()); // 400 Bad Request
    }

    @Test
    @TestDescription("?????? ?????? ???????????? ????????? ????????? ???????????? ?????????")
    public void createEvent_Bad_Request_Empty_Input() throws Exception{
        EventDto eventDto = EventDto.builder().build();

        this.mockMvc.perform(post("/api/events")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("?????? ?????? ????????? ????????? ????????? ???????????? ?????????")
    public void createEvent_Bad_Request_Wrong_Input() throws Exception{
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("Study REST API with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2021, 6, 21, 18, 00))
                .closeEnrollmentDateTime(LocalDateTime.of(2021, 6, 25, 18, 00))
                .beginEventDateTime(LocalDateTime.of(2021, 6, 23, 18, 00))
                .endEventDateTime(LocalDateTime.of(2021, 6, 25, 18, 00))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("???????????????")
                .build();

        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors[0].field").exists())
                .andExpect(jsonPath("errors[0].defaultMessage").exists())
                .andExpect(jsonPath("errors[0].code").exists())
                .andExpect(jsonPath("_links.index").exists());
    }

    @Test
    @TestDescription("30?????? ???????????? 10?????? ??? ?????? ????????? ????????????")
    public void queryEvents() throws Exception{
        // Given
        IntStream.range(0, 30).forEach(this::generateEvent);

        // When
        this.mockMvc.perform(get("/api/events")
                    .param("page", "1")
                    .param("size", "10")
                    .param("sort", "name,DESC")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("query-events"));
    }

    private Event generateEvent(int index){
        Event event = Event.builder()
                .name("Spring")
                .description("test event")
                .beginEnrollmentDateTime(LocalDateTime.of(2021, 6, 13, 18, 00))
                .closeEnrollmentDateTime(LocalDateTime.of(2021, 6, 13, 18, 00))
                .beginEventDateTime(LocalDateTime.of(2021, 6, 13, 18, 00))
                .endEventDateTime(LocalDateTime.of(2021, 6, 13, 18, 00))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("???????????????")
                .free(false)
                .offline(true)
                .eventStatus(EventStatus.DRAFT)
                .build();

        return this.eventRepository.save(event);
    }

    @Test
    @TestDescription("????????? ???????????? ?????? ????????????")
    public void getEvent() throws Exception{
        // Given
        Event event = this.generateEvent(100);

        // When & Then
        this.mockMvc.perform(get("/api/events/{id}", event.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("get-an-event"));
    }

    @Test
    @TestDescription("?????? ???????????? ???????????? ??? 404 ????????????")
    public void getEvent404() throws Exception{
        this.mockMvc.perform(get("/api/events/12345"))
                .andExpect(status().isNotFound());
    }

    @Test
    @TestDescription("???????????? ??????????????? ????????????")
    public void updateEvent() throws Exception {
        // Given
        Event event = this.generateEvent(200);
        EventDto eventDto = this.modelMapper.map(event, EventDto.class);
        String eventName = "Updated Event";
        eventDto.setName(eventName);

        // When & Then
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                        .contentType(APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto))
                    )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(eventName))
                .andExpect(jsonPath("_links.self").exists())
                .andDo(document("update-event"));
    }

    @Test
    @TestDescription("?????? ?????? ???????????? ????????? ????????? ?????? ??????")
    public void updateEvent400_Empty() throws Exception {
        // Given
        Event event = this.generateEvent(200);
        EventDto eventDto = new EventDto();

        // When & Then
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                        .contentType(APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto))
                    )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("?????? ?????? ????????? ????????? ????????? ?????? ??????")
    public void updateEvent400_Wrong() throws Exception {
        // Given
        Event event = this.generateEvent(200);
        EventDto eventDto = this.modelMapper.map(event, EventDto.class);
        eventDto.setBasePrice(20000);
        eventDto.setMaxPrice(1000);

        // When & Then
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                        .contentType(APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto))
                    )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("???????????? ?????? ????????? ?????? ??????")
    public void updateEvent404() throws Exception {
        // Given
        Event event = this.generateEvent(200);
        EventDto eventDto = this.modelMapper.map(event, EventDto.class);

        // When & Then
        this.mockMvc.perform(put("/api/events/12345")
                        .contentType(APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto))
                    )
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
