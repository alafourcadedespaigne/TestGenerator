package utils;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import model.Question;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PDF {

    public static JasperPrint generateDynamicListReportForJRXML(List<?> data, List<AbstractColumn> columns, Map<String, Object> params) throws JRException {
        DynamicReportBuilder drb = new DynamicReportBuilder();
        drb.setUseFullPageWidth(true); //we tell the report to use the full width of the page. this rezises
//the columns width proportionally to meat the page width.

        for (AbstractColumn column : columns) {
            drb.addColumn(column);
        }

        DynamicReport dr = drb.build(); //Finally build the report!
        JRDataSource ds = new JRBeanCollectionDataSource(data);
        return DynamicJasperHelper.generateJasperPrint(dr, new ClassicLayoutManager(), ds, params);
    }

    public static void main(String[] as) {


        List<Question> questions = new ArrayList<>();

        questions.add(new Question("asd", 0, new ArrayList<>()));

        List<AbstractColumn> abstractColumns = new ArrayList<>();

        abstractColumns.add(ColumnBuilder.getNew().setColumnProperty("name", String.class.getName()).build());

        try {

            JasperPrint jasperPrint = generateDynamicListReportForJRXML(questions, abstractColumns, null);

            String outputFilename = "prueba.pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, outputFilename);
        } catch (JRException e) {
            e.printStackTrace();
        }
    }
}
