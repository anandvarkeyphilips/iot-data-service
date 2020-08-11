package io.exnihilo.nearestplaces.service;

import io.exnihilo.nearestplaces.entity.DnsRecord;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * RecordService helps you add,update,modify dns records in DNS server.
 *
 * @author Anand Varkey Philips
 * @date 27/10/2018
 * @since 2.0.0.RELEASE
 */
@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class RecordServiceTest {

  @Mock
  private DnsRecordRepository dnsRecordRepository;
  @InjectMocks
  private IRecordService recordService = new RecordService();

  @Before
  public void setUp() {
    log.info("Any setup to be done before test methods can be done here");
  }

  @Test
  public void getAllRecords_whenHasNoRecordsInDb_returnRecords() throws Exception {
    Mockito.when(dnsRecordRepository.findAll()).thenReturn(new ArrayList<>());

    final List<DnsRecord> allRecords = recordService.getAllRecords();

    Assert.assertEquals(0, allRecords.size());
  }

  @Test
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
    Mockito.when(dnsRecordRepository.findAll()).thenReturn(dnsRecordList);

    final List<DnsRecord> responseDnsRecords = recordService.getAllRecords();

    Assert.assertEquals(dnsRecordList, responseDnsRecords);
    Assert.assertEquals(7, responseDnsRecords.size());
    Mockito.verify(dnsRecordRepository, Mockito.times(1)).findAll();
  }

  @Test
  public void getRecords_whenHasNoRecordsInDbOfFilterCondition_returnRecords() throws Exception {
    Mockito.when(dnsRecordRepository.findByQnameAndQtype("example.com", "NS")).thenReturn(new ArrayList<>());

    final List<DnsRecord> responseDnsRecords = recordService.getRecords("example.com", "NS");

    Assert.assertEquals(new ArrayList<>(), responseDnsRecords);
    Assert.assertEquals(0, responseDnsRecords.size());
    Mockito.verify(dnsRecordRepository, Mockito.times(1)).findByQnameAndQtype("example.com", "NS");
  }

  @Test
  public void getRecords_whenCalledWithFilterCondition_returnRecords() throws Exception {
    DnsRecord dnsRecord2 = new DnsRecord(2, "example.com", "NS", "ns1.jimdo.com", 3600);
    DnsRecord dnsRecord3 = new DnsRecord(3, "example.com", "NS", "ns2.jimdo.com", 3600);
    List<DnsRecord> dnsRecordList = new ArrayList<>();
    dnsRecordList.add(dnsRecord2);
    dnsRecordList.add(dnsRecord3);
    Mockito.when(dnsRecordRepository.findByQnameAndQtype("example.com", "NS")).thenReturn(dnsRecordList);

    final List<DnsRecord> responseDnsRecords = recordService.getRecords("example.com", "NS");

    Assert.assertEquals(dnsRecordList, responseDnsRecords);
    Assert.assertEquals(2, responseDnsRecords.size());
    Mockito.verify(dnsRecordRepository, Mockito.times(1)).findByQnameAndQtype("example.com", "NS");
  }

  @Test
  public void getRecords_whenCalledWithANYType_returnRecords() throws Exception {
    DnsRecord dnsRecord1 = new DnsRecord(1, "example.com", "A", "1.2.3.4", 3600);
    DnsRecord dnsRecord2 = new DnsRecord(2, "example.com", "NS", "ns1.jimdo.com", 3600);
    DnsRecord dnsRecord3 = new DnsRecord(3, "example.com", "NS", "ns2.jimdo.com", 3600);
    DnsRecord dnsRecord4 = new DnsRecord(4, "example.com", "SOA",
        "ns1.jimdo.com. hostmaster.jimdo.com. 2018041355 10800 3600 604800 600", 3600);
    DnsRecord dnsRecord5 = new DnsRecord(5, "example.com", "MX", "mail.jimdo.com", 3600);
    List<DnsRecord> dnsRecordList = new ArrayList<>();
    dnsRecordList.add(dnsRecord1);
    dnsRecordList.add(dnsRecord2);
    dnsRecordList.add(dnsRecord3);
    dnsRecordList.add(dnsRecord4);
    dnsRecordList.add(dnsRecord5);
    Mockito.when(dnsRecordRepository.findByQname("example.com")).thenReturn(dnsRecordList);

    final List<DnsRecord> responseDnsRecords = recordService.getRecords("example.com", "ANY");

    Assert.assertEquals(dnsRecordList, responseDnsRecords);
    Assert.assertEquals(5, responseDnsRecords.size());
    Mockito.verify(dnsRecordRepository, Mockito.times(1)).findByQname("example.com");
  }

  @Test
  public void addRecord_whenAddNewRecordsInDb_returnSavedRecord() throws Exception {
    DnsRecord dnsRecord = new DnsRecord(2, "example.com", "NS", "ns1.jimdo.com", 3600);
    Mockito.when(dnsRecordRepository.save(dnsRecord)).thenReturn(dnsRecord);

    final DnsRecord responseDnsRecord = recordService.addRecord(dnsRecord);

    Assert.assertEquals(dnsRecord, responseDnsRecord);
    Mockito.verify(dnsRecordRepository, Mockito.times(1)).save(dnsRecord);
  }

  @Test
  public void updateRecord_whenRecordsExistInDb_returnUpdatedRecord() throws Exception {
    DnsRecord dnsRecord = new DnsRecord(2, "example.com", "NS", "ns1.jimdo.com", 3600);
    Mockito.when(dnsRecordRepository.existsById(dnsRecord.getId())).thenReturn(true);
    Mockito.when(dnsRecordRepository.save(dnsRecord)).thenReturn(dnsRecord);

    final DnsRecord responseDnsRecord = recordService.updateRecord(dnsRecord);

    Assert.assertEquals(dnsRecord, responseDnsRecord);
    Mockito.verify(dnsRecordRepository, Mockito.times(1)).existsById(dnsRecord.getId());
    Mockito.verify(dnsRecordRepository, Mockito.times(1)).save(dnsRecord);
  }

  @Test
  public void updateRecord_whenRecordsNotExistInDb_throwException() throws Exception {
    DnsRecord dnsRecord = new DnsRecord(2, "example.com", "NS", "ns1.jimdo.com", 3600);
    Mockito.when(dnsRecordRepository.existsById(dnsRecord.getId())).thenReturn(false);

    Exception exception = Assert.assertThrows(EntityNotFoundException.class, () -> {
      final DnsRecord responseDnsRecord = recordService.updateRecord(dnsRecord);
    });

    Assert.assertEquals("Entity with id:" + dnsRecord.getId() + " was not found", exception.getMessage());
    Mockito.verify(dnsRecordRepository, Mockito.times(1)).existsById(dnsRecord.getId());
  }

  @Test
  public void deleteRecord_whenRecordsExistInDb_returnUpdatedRecord() throws Exception {
    DnsRecord dnsRecord = new DnsRecord(2, "example.com", "NS", "ns1.jimdo.com", 3600);
    Mockito.when(dnsRecordRepository.existsById(dnsRecord.getId())).thenReturn(true);
    Mockito.doNothing().when(dnsRecordRepository).deleteById(dnsRecord.getId());

    recordService.deleteRecord(dnsRecord.getId());

    Mockito.verify(dnsRecordRepository, Mockito.times(1)).existsById(dnsRecord.getId());
    Mockito.verify(dnsRecordRepository, Mockito.times(1)).deleteById(dnsRecord.getId());
  }

  @Test
  public void deleteRecord_whenRecordsNotExistInDb_throwException() throws Exception {
    DnsRecord dnsRecord = new DnsRecord(2, "example.com", "NS", "ns1.jimdo.com", 3600);
    Mockito.when(dnsRecordRepository.existsById(dnsRecord.getId())).thenReturn(false);

    Exception exception = Assert.assertThrows(EntityNotFoundException.class, () -> {
      recordService.deleteRecord(dnsRecord.getId());
    });

    Assert.assertEquals("Entity with id:" + dnsRecord.getId() + " was not found", exception.getMessage());
    Mockito.verify(dnsRecordRepository, Mockito.times(1)).existsById(dnsRecord.getId());
  }
}
