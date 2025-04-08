package com.lab.service.impl;

import com.lab.dto.request.TestRequestDTO;
import com.lab.dto.response.TestResponseDTO;
import com.lab.entity.Order;
import com.lab.entity.Test;
import com.lab.entity.TestType;
import com.lab.exception.OrderNotFoundException;
import com.lab.exception.PDFCreationErrorException;
import com.lab.exception.TestNotFoundException;
import com.lab.exception.TestTypeNotFoundException;
import com.lab.mapper.impl.TestMapperImpl;
import com.lab.repository.OrderRepository;
import com.lab.repository.TestRepository;
import com.lab.repository.TestTypeRepository;
import com.lab.service.TestService;
import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;

@Service
public class TestServiceImpl implements TestService {

    private final TestRepository testRepository;
    private final OrderRepository orderRepository;
    private final TestTypeRepository testTypeRepository;
    private final TestMapperImpl testMapperImpl;


    public TestServiceImpl(
            TestRepository testRepository,
            OrderRepository orderRepository,
            TestTypeRepository testTypeRepository,
            TestMapperImpl testMapperImpl
    ) {
        this.testRepository = testRepository;
        this.orderRepository = orderRepository;
        this.testTypeRepository = testTypeRepository;
        this.testMapperImpl = testMapperImpl;
    }

    @Override
    public Page<TestResponseDTO> getAllTests(Pageable pageable) {
        Page<Test> testsPage = testRepository.findAll(pageable);

        if (testsPage.isEmpty()) {
            throw new TestNotFoundException("Лаб. исследований не найдено");
        }

        return testsPage.map(testMapperImpl::toResponseDTO);
    }

    @Cacheable(value = "tests", key = "#id")
    @Override
    public TestResponseDTO getTestById(Long id) {
        Test test = testRepository.findById(id)
                .orElseThrow(() -> new TestNotFoundException("Тест с id-" + id + " не найден"));
        return testMapperImpl.toResponseDTO(test);
    }

    @Override
    public TestResponseDTO createTest(TestRequestDTO testDTO) {
        Order order = orderRepository.findById(testDTO.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException("Заявки с id- "
                        + testDTO.getOrderId() + " не найдено"));

        TestType testType = testTypeRepository.findById(testDTO.getTestTypeId())
                .orElseThrow(() -> new TestTypeNotFoundException("Тип исследования с id- "
                        + testDTO.getTestTypeId() + " не найден"));

        Test test = testMapperImpl.toEntity(testDTO, order, testType);

        test = testRepository.save(test);

        return testMapperImpl.toResponseDTO(test);
    }

    @Override
    @CachePut(value = "tests", key = "#id")
    public TestResponseDTO updateTest(Long id, TestRequestDTO testDTO) {
        Test test = testRepository.findById(id)
                .orElseThrow(() -> new TestNotFoundException("Тест с id-" + id + " не найден"));

        test.setExecutionDate(LocalDateTime.now());
        test.setResult(testDTO.getResult());
        test.setReferenceValues(testDTO.getReferenceValues());
        test.setStatus(testDTO.getStatus());

        test = testRepository.save(test);
        return testMapperImpl.toResponseDTO(test);
    }

    @Override
    @CachePut(value = "tests", key = "#id")
    public TestResponseDTO updateTestResult(Long id, String newResult) {
        Test test = testRepository.findById(id)
                .orElseThrow(() -> new TestNotFoundException("Тест с id-" + id + " не найден"));

        if (test.getResult() != null && !test.getResult().isEmpty()) {
            test.setResult(test.getResult() + ", " + newResult);
        } else {
            test.setResult(newResult);
        }

        test = testRepository.save(test);
        return testMapperImpl.toResponseDTO(test);
    }

    @Override
    public byte[] generateTestPdf(Long id) {
        Test test = testRepository.findById(id)
                .orElseThrow(() -> new TestNotFoundException("Тест с id-" + id + " не найден"));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, out);
            document.open();


            String fontBoldPath = "src/main/resources/fonts/Roboto-Bold.ttf";
            BaseFont baseBoldFont = BaseFont.createFont(fontBoldPath, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            Font robotoBoldFont = new Font(baseBoldFont, 16, Font.BOLD);

            String titleText = "Лабораторный тест #" + test.getId();
            Paragraph title = new Paragraph(titleText, robotoBoldFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph("\n"));

            document.add(new Paragraph("Тест ID: " + test.getId()));

            document.add(new Paragraph("Заявка:"));

            Paragraph orderInfo = new Paragraph();
            orderInfo.add(new Chunk("ID: " + test.getOrder().getId()));
            orderInfo.setIndentationLeft(20);
            document.add(orderInfo);

            Paragraph patientInfo = new Paragraph();
            patientInfo.add(new Chunk("Пациент: " + test.getOrder().getPatient().getLastName() + " " +
                    test.getOrder().getPatient().getFirstName()));
            patientInfo.setIndentationLeft(20);
            document.add(patientInfo);

            Paragraph createdDateInfo = new Paragraph();
            createdDateInfo.add(new Chunk("Дата создания: " + test.getOrder().getCreatedDate()));
            createdDateInfo.setIndentationLeft(20);
            document.add(createdDateInfo);

            Paragraph statusInfo = new Paragraph();
            statusInfo.add(new Chunk("Статус заявки: " + test.getOrder().getStatus().name()));
            statusInfo.setIndentationLeft(20);
            document.add(statusInfo);

            Paragraph commentInfo = new Paragraph();
            commentInfo.add(new Chunk("Комментарий: " + test.getOrder().getComment()));
            commentInfo.setIndentationLeft(20);
            document.add(commentInfo);

            document.add(new Paragraph("Тип теста: " + test.getTestType().getName()));
            document.add(new Paragraph("Дата выполнения: " + test.getExecutionDate()));
            document.add(new Paragraph("Результат: " + test.getResult()));
            document.add(new Paragraph("Референсные значения: " + test.getReferenceValues()));
            document.add(new Paragraph("Статус теста: " + test.getStatus().name()));

            document.close();
        } catch (Exception e) {
            throw new PDFCreationErrorException("Ошибка генерации PDF");
        }

        return out.toByteArray();
    }
}