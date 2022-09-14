
package services.xml_parser_service;
import services.main_service.Engine;
import xjc_generated_files.AbsDescriptor;
//import xjc_generated_files.AbsDescriptor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.InputStream;

public class XMLParser {
    final private String path_to_file;
    final private Engine server;
    private final InputStream file;
    private String ownerNameFromLogin="";//TODO- MAYBE MOVE TO THE SEVER OR ENGINE

    public XMLParser(String path_to_file, String nameFromLogin, Engine server) {
        this.path_to_file = path_to_file;
        this.server = server;
        this.ownerNameFromLogin = nameFromLogin;
        file = null;
    }

    public XMLParser(InputStream file, String nameFromLogin, Engine server) {
        this.file = file;
        this.server = server;
        this.ownerNameFromLogin = nameFromLogin;
        path_to_file = null;
    }

    public void fromXmlFileToObject() throws JAXBException {

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(AbsDescriptor.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            //File file = new File(this.path_to_file);//FOR PATH

            AbsDescriptor absDescriptor = (AbsDescriptor) jaxbUnmarshaller.unmarshal(this.file);

            absDescriptor.getAbsCategories().getAbsCategory().forEach(categoryName -> {
                try {
                    server.addCategory(categoryName);
                } catch (Exception e) {
                    //server.initEngine();
                    System.out.println(e);
                    throw new RuntimeException(e);
                }
            });


            /*
            //Customers
            absDescriptor.getAbsCustomers().getAbsCustomer().forEach(customer -> {
                try {
                    server.addCustomer(customer.getName(), customer.getAbsBalance());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    server.initEngine();
                    throw new RuntimeException(e);

                }

            });//not relevnat to part3*/

            // Loans
            absDescriptor.getAbsLoans().getAbsLoan().forEach(loan -> {
                try {
                    server.addLoan(loan.getId(), this.ownerNameFromLogin, loan.getAbsCategory(), loan.getAbsCapital(), loan.getAbsTotalYazTime(), loan.getAbsPaysEveryYaz(), loan.getAbsIntristPerPayment());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    //server.initEngine();
                    throw new RuntimeException(e);
                }
            });

        }catch (Exception e){
            throw (e);}

    }
}
