package com.cuupa.mailprocessor.delegates;

import com.cuupa.mailprocessor.process.ProcessInstanceHandler;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlaintextDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        ProcessInstanceHandler handler = new ProcessInstanceHandler(delegateExecution);

        try (PDDocument document = PDDocument.load(new ByteArrayInputStream(handler.getFileContent()))) {
            handler.setPlaintext(getTextPerPage(document));
        }
    }

    private List<String> getTextPerPage(PDDocument document) throws IOException {
        final List<String> pages = new ArrayList<>(document.getNumberOfPages());
        for (int page = 0; page <= document.getNumberOfPages(); page++) {
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setStartPage(page);
            stripper.setEndPage(page);
            pages.add(stripper.getText(document));
        }
        return pages;
    }
}
