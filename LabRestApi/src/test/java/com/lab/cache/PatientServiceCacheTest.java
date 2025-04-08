//package com.lab.cache;
//
//import com.github.benmanes.caffeine.cache.Caffeine;
//import com.lab.cache.impl.CacheServiceImpl;
//import com.lab.dto.request.PatientRequestDTO;
//import com.lab.dto.response.PatientResponseDTO;
//import com.lab.entity.Patient;
//import com.lab.exception.PatientNotFoundException;
//import com.lab.mapper.impl.OrderMapperImpl;
//import com.lab.mapper.impl.PatientMapperImpl;
//import com.lab.repository.OrderRepository;
//import com.lab.repository.PatientRepository;
//import com.lab.service.PatientService;
//import com.lab.service.impl.PatientServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.cache.CacheManager;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.cache.caffeine.CaffeineCacheManager;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Primary;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//import java.util.concurrent.TimeUnit;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(SpringExtension.class)
//@EnableCaching
//public class PatientServiceCacheTest {
//
//    @TestConfiguration
//    static class TestConfig {
//        @Bean
//        @Primary
//        public PatientRepository patientRepository() {
//            return Mockito.mock(PatientRepository.class);
//        }
//
//        @Bean
//        @Primary
//        public OrderRepository orderRepository() {
//            return Mockito.mock(OrderRepository.class);
//        }
//
//        @Bean
//        @Primary
//        public PatientMapperImpl patientMapperImpl() {
//            return new PatientMapperImpl();
//        }
//
//        @Bean
//        @Primary
//        public OrderMapperImpl orderMapperImpl() {
//            return new OrderMapperImpl();
//        }
//
//        @Bean
//        @Primary
//        public CacheServiceImpl patientCacheServiceImpl() {
//            return new CacheServiceImpl(еу);
//        }
//
//        @Bean
//        public PatientServiceImpl patientServiceImpl() {
//            return new PatientServiceImpl(
//                    patientRepository(),
//                    orderRepository(),
//                    patientMapperImpl(),
//                    orderMapperImpl(),
//                    patientCacheServiceImpl()
//            );
//        }
//
//        @Bean
//        public CacheManager testCacheManager() {
//            CaffeineCacheManager cacheManager = new CaffeineCacheManager(
//                    "patients",
//                    "patientsByFio",
//                    "patientsByBirthDate");
//            cacheManager.setCaffeine(Caffeine.newBuilder()
//                    .maximumSize(1000)
//                    .expireAfterWrite(1, TimeUnit.HOURS));
//            return cacheManager;
//        }
//    }
//
//    @Autowired
//    private PatientService patientServiceImpl;
//
//    @Autowired
//    private PatientRepository patientRepository;
//
//    @Autowired
//    private CacheManager cacheManager;
//
//    @BeforeEach
//    void clearCache() {
//        cacheManager.getCache("patients").clear();
//    }
//
//    @Test
//    void getPatient_shouldCacheResult() {
//
//        Long patientId = 2L;
//        Patient patient = new Patient();
//        patient.setId(patientId);
//        patient.setLastName("Nikitov");
//        patient.setFirstName("Nikita");
//
//
//        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
//        PatientResponseDTO response1 = patientServiceImpl.getPatient(patientId);
//        assertEquals("Nikitov", response1.getLastName());
//
//
//        PatientResponseDTO response2 = patientServiceImpl.getPatient(patientId);
//        assertEquals("Nikitov", response2.getLastName());
//
//        verify(patientRepository, times(1)).findById(patientId);
//    }
//
//    @Test
//    void updatePatient_shouldUpdateCache() {
//        Long patientId = 1L;
//
//        Patient existing = new Patient();
//        existing.setId(patientId);
//        existing.setLastName("Ivanov");
//
//        when(patientRepository.findById(patientId)).thenReturn(Optional.of(existing));
//        when(patientRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
//
//        PatientRequestDTO updatedDto = new PatientRequestDTO();
//        updatedDto.setLastName("Petrov");
//
//        patientServiceImpl.updatePatient(patientId, updatedDto);
//
//        when(patientRepository.findById(patientId)).thenThrow(new AssertionError("Должно вернуться из кэша"));
//
//        PatientResponseDTO cached = patientServiceImpl.getPatient(patientId);
//        assertEquals("Petrov", cached.getLastName());
//
//        verify(patientRepository, times(1)).findById(patientId);
//        verify(patientRepository, times(1)).save(any(Patient.class));
//    }
//
//    @Test
//    void searchPatients_shouldCacheResult() {
//        String lastName = "Sidorov";
//        String firstName = "Ivan";
//        String middleName = "Petrovich";
//
//        List<Patient> patients = List.of(new Patient(1L, lastName, firstName, middleName, null, null, null, null));
//        when(patientRepository.findByLastNameAndFirstNameAndMiddleName(lastName, firstName, middleName))
//                .thenReturn(patients);
//
//        List<PatientResponseDTO> result1 = patientServiceImpl.searchPatients(lastName, firstName, middleName);
//        List<PatientResponseDTO> result2 = patientServiceImpl.searchPatients(lastName, firstName, middleName);
//
//        assertEquals(1, result1.size());
//        assertEquals("Sidorov", result1.get(0).getLastName());
//        assertEquals(result1.get(0), result2.get(0));
//
//        verify(patientRepository, times(1))
//                .findByLastNameAndFirstNameAndMiddleName(lastName, firstName, middleName);
//    }
//
//    @Test
//    void searchPatientsByBirthDate_shouldCacheResult() {
//        LocalDate birthDate = LocalDate.of(1990, 1, 1);
//
//        List<Patient> patients = List.of(new Patient(1L, "Kozlov", "Petr", "Alekseevich", birthDate, null, null, null));
//        when(patientRepository.findByBirthDate(birthDate)).thenReturn(patients);
//
//        List<PatientResponseDTO> result1 = patientServiceImpl.searchPatientsByBirthDate(birthDate);
//        List<PatientResponseDTO> result2 = patientServiceImpl.searchPatientsByBirthDate(birthDate);
//
//        assertEquals(1, result1.size());
//        assertEquals("Kozlov", result1.get(0).getLastName());
//        assertEquals(result1.get(0), result2.get(0));
//
//        verify(patientRepository, times(1)).findByBirthDate(birthDate);
//    }
//
//
//    @Test
//    void deletePatient_shouldEvictFromCache() {
//        Long patientId = 3L;
//        Patient patient = new Patient();
//        patient.setId(patientId);
//        patient.setLastName("Ivanov");
//
//        when(patientRepository.findById(patientId))
//                .thenReturn(Optional.of(patient))
//                .thenReturn(Optional.of(patient))
//                .thenReturn(Optional.empty());
//
//        PatientResponseDTO response1 = patientServiceImpl.getPatient(patientId);
//        assertEquals("Ivanov", response1.getLastName());
//
//        patientServiceImpl.deletePatient(patientId);
//
//        assertThrows(PatientNotFoundException.class, () -> {
//            patientServiceImpl.getPatient(patientId);
//        });
//    }
//
//}