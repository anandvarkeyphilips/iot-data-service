package io.exnihilo.nearestplaces.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.exnihilo.nearestplaces.exceptionhandling.handler.RestExceptionHandler;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DnsRecordControllerIntegrationTest {

  private MockMvc mockMvc;
  @Autowired
  private TestRestTemplate restTemplate;
  @Autowired
  private DnsRecordController dnsRecordController;
  @Autowired
  private DnsRecordRepository dnsRecordRepository;

  @Before
  public void setUp() throws Exception {
    mockMvc =
        MockMvcBuilders.standaloneSetup(dnsRecordController).setControllerAdvice(new RestExceptionHandler()).build();
  }

  @Test
  public void getAllRecords_whenHasNoRecordsInDb_returnRecords() throws Exception {
    mockMvc.perform(get("/dns-records").contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk()).andExpect(jsonPath("$.result", Matchers.hasSize(0)));
  }

  @Test
  @DirtiesContext
  public void getAllRecords_whenHasRecordsInDb_returnRecords() throws Exception {
    DnsRecord dnsRecord1 = new DnsRecord(1, "example.com", "A", "1.2.3.4", 3600);
    DnsRecord dnsRecord2 = new DnsRecord(2, "example.com", "NS", "ns1.jimdo.com", 3600);
    DnsRecord dnsRecord3 = new DnsRecord(3, "example.com", "NS", "ns2.jimdo.com", 3600);
    DnsRecord dnsRecord4 = new DnsRecord(4, "example.com", "SOA",
        "ns1.jimdo.com. hostmaster.jimdo.com. 2018041355 10800 3600 604800 600", 3600);
    DnsRecord dnsRecord5 = new DnsRecord(5, "example.com", "MX", "mail.jimdo.com", 3600);
    DnsRecord dnsRecord6 = new DnsRecord(6, "example1.com", "A", "192.168.1.1", 3600);
    DnsRecord dnsRecord7 = new DnsRecord(7, "example2.com", "A", "192.168.1.1", 3600);
    List<DnsRecord> dnsRecordList = new ArrayList<>();
    dnsRecordList.add(dnsRecord1);
    dnsRecordList.add(dnsRecord2);
    dnsRecordList.add(dnsRecord3);
    dnsRecordList.add(dnsRecord4);
    dnsRecordList.add(dnsRecord5);
    dnsRecordList.add(dnsRecord6);
    dnsRecordList.add(dnsRecord7);
    dnsRecordRepository.saveAll(dnsRecordList);

    final ResultActions resultActions = mockMvc.perform(get("/dns-records").contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print()).andExpect(status().isOk())
        .andExpect(jsonPath("$.result", Matchers.hasSize(7)));

    ObjectMapper objectMapper = new ObjectMapper();
    final ResultList resultList =
        objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), ResultList.class);
    Assert.assertEquals(dnsRecordList, resultList.getResult());
    Assert.assertEquals(7, resultList.getResult().size());
  }

  @Test
  public void getRecords_whenHasNoRecordsInDbOfFilterCondition_returnRecords() throws Exception {
    mockMvc.perform(get("/dns-records/lookup/example7.com/NS").contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print()).andExpect(status().isOk())
        .andExpect(jsonPath("$.result", Matchers.hasSize(0)));
  }

  @Test
  @DirtiesContext
  public void getRecords_whenCalledWithFilterCondition_returnRecords() throws Exception {
    DnsRecord dnsRecord1 = new DnsRecord(1, "example.com", "A", "1.2.3.4", 3600);
    DnsRecord dnsRecord2 = new DnsRecord(2, "example.com", "NS", "ns1.jimdo.com", 3600);
    DnsRecord dnsRecord3 = new DnsRecord(3, "example.com", "NS", "ns2.jimdo.com", 3600);
    DnsRecord dnsRecord4 = new DnsRecord(4, "example.com", "SOA",
        "ns1.jimdo.com. hostmaster.jimdo.com. 2018041355 10800 3600 604800 600", 3600);
    DnsRecord dnsRecord5 = new DnsRecord(5, "example.com", "MX", "mail.jimdo.com", 3600);
    DnsRecord dnsRecord6 = new DnsRecord(6, "example1.com", "A", "192.168.1.1", 3600);
    DnsRecord dnsRecord7 = new DnsRecord(7, "example2.com", "A", "192.168.1.1", 3600);
    List<DnsRecord> dnsRecordList = new ArrayList<>();
    dnsRecordList.add(dnsRecord1);
    dnsRecordList.add(dnsRecord2);
    dnsRecordList.add(dnsRecord3);
    dnsRecordList.add(dnsRecord4);
    dnsRecordList.add(dnsRecord5);
    dnsRecordList.add(dnsRecord6);
    dnsRecordList.add(dnsRecord7);
    dnsRecordRepository.saveAll(dnsRecordList);

    final ResultActions resultActions =
        mockMvc.perform(get("/dns-records/lookup/example.com/NS").contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print()).andExpect(status().isOk())
            .andExpect(jsonPath("$.result", Matchers.hasSize(2)));

    ObjectMapper objectMapper = new ObjectMapper();
    final ResultList resultList =
        objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), ResultList.class);
    List<DnsRecord> expectedResponseDnsRecordList = new ArrayList<>();
    expectedResponseDnsRecordList.add(dnsRecord2);
    expectedResponseDnsRecordList.add(dnsRecord3);

    Assert.assertEquals(expectedResponseDnsRecordList, resultList.getResult());
    Assert.assertEquals(2, resultList.getResult().size());
  }

  @Test
  public void getRecords_whenCalledWithANYType_returnRecords() throws Exception {
    DnsRecord dnsRecord1 = new DnsRecord(1, "example.com", "A", "1.2.3.4", 3600);
    DnsRecord dnsRecord2 = new DnsRecord(2, "example.com", "NS", "ns1.jimdo.com", 3600);
    DnsRecord dnsRecord3 = new DnsRecord(3, "example.com", "NS", "ns2.jimdo.com", 3600);
    DnsRecord dnsRecord4 = new DnsRecord(4, "example.com", "SOA",
        "ns1.jimdo.com. hostmaster.jimdo.com. 2018041355 10800 3600 604800 600", 3600);
    DnsRecord dnsRecord5 = new DnsRecord(5, "example.com", "MX", "mail.jimdo.com", 3600);
    DnsRecord dnsRecord6 = new DnsRecord(6, "example1.com", "A", "192.168.1.1", 3600);
    DnsRecord dnsRecord7 = new DnsRecord(7, "example2.com", "A", "192.168.1.1", 3600);
    List<DnsRecord> dnsRecordList = new ArrayList<>();
    dnsRecordList.add(dnsRecord1);
    dnsRecordList.add(dnsRecord2);
    dnsRecordList.add(dnsRecord3);
    dnsRecordList.add(dnsRecord4);
    dnsRecordList.add(dnsRecord5);
    dnsRecordList.add(dnsRecord6);
    dnsRecordList.add(dnsRecord7);
    dnsRecordRepository.saveAll(dnsRecordList);

    final ResultActions resultActions =
        mockMvc.perform(get("/dns-records/lookup/example.com/ANY").contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print()).andExpect(status().isOk())
            .andExpect(jsonPath("$.result", Matchers.hasSize(5)));

    ObjectMapper objectMapper = new ObjectMapper();
    final ResultList resultList =
        objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), ResultList.class);
    List<DnsRecord> expectedResponseDnsRecordList = new ArrayList<>();
    expectedResponseDnsRecordList.add(dnsRecord1);
    expectedResponseDnsRecordList.add(dnsRecord2);
    expectedResponseDnsRecordList.add(dnsRecord3);
    expectedResponseDnsRecordList.add(dnsRecord4);
    expectedResponseDnsRecordList.add(dnsRecord5);

    Assert.assertEquals(expectedResponseDnsRecordList, resultList.getResult());
    Assert.assertEquals(5, resultList.getResult().size());
  }

  @Test
  @DirtiesContext
  public void addRecord_whenAddNewRecordsInDb_returnSavedRecord() throws Exception {
    DnsRecord dnsRecord = new DnsRecord(1, "example.com", "A", "1.2.3.4", 3600);

    ObjectMapper objectMapper = new ObjectMapper();
    String objectJsonString = objectMapper.writeValueAsString(dnsRecord);

    final ResultActions resultActions =
        mockMvc.perform(post("/dns-records").contentType(MediaType.APPLICATION_JSON).content(objectJsonString))
            .andDo(MockMvcResultHandlers.print()).andExpect(status().isOk());

    final DnsRecord responseDnsRecord =
        objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), DnsRecord.class);
    Assert.assertEquals(dnsRecord, responseDnsRecord);
    Assert.assertEquals(1, dnsRecordRepository.count());
  }

  @Test
  @DirtiesContext
  public void updateRecord_whenRecordsExistInDb_returnUpdatedRecord() throws Exception {
    DnsRecord dnsRecord = new DnsRecord(1, "example.com", "A", "1.2.3.4", 3600);
    dnsRecordRepository.save(dnsRecord);
    Assert.assertEquals(1, dnsRecordRepository.count());

    DnsRecord updateDnsRecord = new DnsRecord(1, "example8.com", "A", "1.2.3.4", 3600);
    ObjectMapper objectMapper = new ObjectMapper();
    String objectJsonString = objectMapper.writeValueAsString(updateDnsRecord);

    final ResultActions resultActions =
        mockMvc.perform(put("/dns-records/1").contentType(MediaType.APPLICATION_JSON).content(objectJsonString))
            .andDo(MockMvcResultHandlers.print()).andExpect(status().isOk());

    final DnsRecord responseDnsRecord =
        objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), DnsRecord.class);
    Assert.assertEquals(updateDnsRecord, responseDnsRecord);
    Assert.assertEquals(1, dnsRecordRepository.count());

  }

  @Test
  public void updateRecord_whenRecordsNotExistInDb_throwException() throws Exception {
    Assert.assertEquals(0, dnsRecordRepository.count());

    DnsRecord updateDnsRecord = new DnsRecord(1, "example8.com", "A", "1.2.3.4", 3600);
    ObjectMapper objectMapper = new ObjectMapper();
    String objectJsonString = objectMapper.writeValueAsString(updateDnsRecord);

    mockMvc.perform(put("/dns-records/1").contentType(MediaType.APPLICATION_JSON).content(objectJsonString))
        .andExpect(status().isNotFound());

    Assert.assertEquals(0, dnsRecordRepository.count());
  }

  @Test
  public void deleteRecord_whenRecordsExistInDb_returnUpdatedRecord() throws Exception {
    DnsRecord dnsRecord = new DnsRecord(1, "example.com", "A", "1.2.3.4", 3600);
    dnsRecordRepository.save(dnsRecord);
    Assert.assertEquals(1, dnsRecordRepository.count());

    final ResultActions resultActions =
        mockMvc.perform(delete("/dns-records/1").contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print()).andExpect(status().isOk());
    Assert.assertEquals(0, dnsRecordRepository.count());
  }

  @Test
  public void deleteRecord_whenRecordsNotExistInDb_throwException() throws Exception {
    Assert.assertEquals(0, dnsRecordRepository.count());

    DnsRecord updateDnsRecord = new DnsRecord(1, "example8.com", "A", "1.2.3.4", 3600);
    ObjectMapper objectMapper = new ObjectMapper();
    String objectJsonString = objectMapper.writeValueAsString(updateDnsRecord);

    mockMvc.perform(delete("/dns-records/1").contentType(MediaType.APPLICATION_JSON).content(objectJsonString))
        .andExpect(status().isNotFound());

    Assert.assertEquals(0, dnsRecordRepository.count());
  }
}
