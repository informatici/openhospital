package org.isf.utils.db;

import java.io.File;
import java.io.FileInputStream;
import java.util.Hashtable;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.isf.dicom.model.FileDicomBase;
import org.isf.dicom.model.FileDicomDetail;
import org.isf.examination.model.PatientExamination;
import org.isf.generaldata.MessageBundle;
import org.isf.patient.model.Patient;
import org.isf.sms.model.Sms;
import org.isf.supplier.model.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pietro Castellucci
 * @version 0.1.0
 */
public class HybernateSessions {
	
	protected Logger logger = LoggerFactory.getLogger(HybernateSessions.class);

    private static Hashtable<String, SessionFactory> mapSessionFactory = new Hashtable<String, SessionFactory>();

    private static synchronized void makeSessionFactory(String name)
     {
        try
        {
            Properties props = new Properties();
            File f = new File("rsc/"+name);
            if (!f.exists())
            {
				JOptionPane.showMessageDialog(null, 
						MessageBundle.getMessage("angal.dicom.nofile") + ":\nrsc/" + name);
                System.exit(-100);
            }
            FileInputStream in = new FileInputStream("rsc/"+name);
            props.load(in);
            in.close();

            Configuration cfg = new Configuration();

            cfg = cfg.setProperties(props);

            cfg = cfg.addPackage("org.isf.examination.model").addAnnotatedClass(PatientExamination.class);
            cfg = cfg.addPackage("org.isf.patient.model").addAnnotatedClass(Patient.class);
            cfg = cfg.addPackage("org.isf.supplier.model").addAnnotatedClass(Supplier.class);
            cfg = cfg.addPackage("org.isf.dicom.model")
            	.addClass(FileDicomBase.class)
            	.addClass(FileDicomDetail.class);
            cfg = cfg.addPackage("org.isf.sms.model").addAnnotatedClass(Sms.class);
            
            ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(cfg.getProperties()).buildServiceRegistry();
            SessionFactory sessions = cfg.buildSessionFactory(serviceRegistry);

            mapSessionFactory.put(name,sessions);
        }
        catch (Exception ecc)
        {
            ecc.printStackTrace();
			JOptionPane.showMessageDialog(null, 
					MessageBundle.getMessage("angal.dicom.noconn") + ":\n" + ecc.getMessage());
            System.exit(-100);
        }
     }

   /**
    * Returns a Hibernate Database Session
    * @param hybernateConfigurationProperties, name of the Hibernate configuration file for the session in rsc
    * @return hybernate session
    */

    public static synchronized Session getSession(String hybernateConfigurationProperties)
    {
         Session ses = null;
         String name = hybernateConfigurationProperties;
         try
         {
              if (!name.endsWith(".properties"))
                name = name + ".properties";


              if (!mapSessionFactory.containsKey(name))
                  makeSessionFactory(name);


              SessionFactory sessFact =  (SessionFactory ) mapSessionFactory.get(name);

              ses = sessFact.getCurrentSession();
         }
         catch (Exception ecc)
         {
             ecc.printStackTrace();
             System.exit(-100);
         }

         return ses;
        
    }
}
