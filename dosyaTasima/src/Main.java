//Dosya Taşıma,Şifreleme ve Gizleme Uygulaması

//Arayüz islemleri icin kütüphaneler
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//Dosya islemleri icin kütüphaneler
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.DosFileAttributeView;
import java.security.Key;


public class Main {
    public static void main(String[] args) {
        Arayüz a1 = new Arayüz();
        a1.ArayüzIslemleri();
    }
}




class Arayüz {

    String TasinacakDizin="";
    String HedefDizin="";
    public void ArayüzIslemleri(){
        Dosyaİslemleri dosyaislem=new Dosyaİslemleri();

        //Arayüzümüzü oluşturuyoruz.
        JFrame jf = new JFrame("KlasörAP");
        jf.setSize(600, 400);
        jf.setLocation(250, 250);

        //Ekrana yazı yazdırma işlemini yapıyoruz.
        JLabel TasınacakDizinLabel = new JLabel("");
        TasınacakDizinLabel.setBounds(20, 80, 400, 20);
        jf.add(TasınacakDizinLabel);

        JLabel YeniKonumLabel = new JLabel("");
        YeniKonumLabel.setBounds(20, 150, 400, 20);
        jf.add(YeniKonumLabel);

        //Kaynak Klasörü Seç butonunu arayüze ekliyoruz
        JButton kaynakDosyaSec = new JButton("Kaynak Klasörü Seç");
        kaynakDosyaSec.setBounds(20,40, 150, 30);
        jf.add(kaynakDosyaSec);
        kaynakDosyaSec.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                JFileChooser jfc = new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                jfc.showOpenDialog(null);

                TasınacakDizinLabel.setText(String.valueOf(jfc.getSelectedFile()));
                TasinacakDizin=String.valueOf(jfc.getSelectedFile());


            }
        });



        //Hedef Dosya Seç butonunu arayüze ekliyoruz
        JButton hedefDosyaSec = new JButton("Hedef Klasörü Seç");
        hedefDosyaSec.setBounds(20,110, 150, 30);
        jf.add(hedefDosyaSec);
        hedefDosyaSec.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                JFileChooser jfc2 = new JFileChooser();
                jfc2.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                jfc2.showOpenDialog(null);
                YeniKonumLabel.setText(String.valueOf(jfc2.getSelectedFile()));
                HedefDizin=String.valueOf(jfc2.getSelectedFile());

            }
        });

        //Sifrele ve Gizle tercihlerini arayüze ekliyoruz
        JRadioButton Sifrele = new JRadioButton("Şifrele");
        JRadioButton Gizle = new JRadioButton("Gizle");
        Sifrele.setBounds(60, 220, 100, 30);
        Gizle.setBounds(60, 260, 100, 30);
        jf.add(Sifrele);
        Sifrele.setBackground(Color.DARK_GRAY);
        Sifrele.setForeground(Color.WHITE);
        jf.add(Gizle);
        Gizle.setBackground(Color.DARK_GRAY);
        Gizle.setForeground(Color.WHITE);

        //Dosya türü seçeneğini arayüze ekliyoruz
        JLabel dosyaCesidiLabel = new JLabel("Dosya Türü");
        String[] dosyacesidi={"Tüm Dosyalar","Sadece txt","Sadece pdf","Sadece png","Sadece docx"};
        JComboBox dosyaCesidi = new JComboBox(dosyacesidi);
        dosyaCesidi.setBounds(410, 70, 130, 30);
        dosyaCesidiLabel.setBounds(410,45,130,30);
        jf.add(dosyaCesidiLabel);
        jf.add(dosyaCesidi);
        dosyaCesidi.setBackground(Color.LIGHT_GRAY);
        dosyaCesidi.setForeground(Color.WHITE);

        //Taşı butonunu arayüze ekliyoruz
        JButton tası = new JButton("Taşı");
        tası.setBounds(400, 250, 150, 50);
        jf.add(tası);

        //Tası butonuna basıldığında yapılacak islemleri olusturuyoruz.
        tası.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {

                //Sifrele Gizle ve Dosya türü seçeneklerini sayılara dönüştürüp fonksiyonun içine gönderiyoruz.
                int sifrele = 0, gizle = 0, dosyatürü = 1;
                if (Sifrele.isSelected()) {
                    sifrele = 1;
                }
                if (Gizle.isSelected()) {
                    gizle = 1;
                }
                if (dosyaCesidi.getSelectedItem() == "Tüm Dosyalar") {
                    dosyatürü = 1;
                }
                else if (dosyaCesidi.getSelectedItem() == "Sadece pdf") {
                    dosyatürü = 2;
                }
                else if (dosyaCesidi.getSelectedItem() == "Sadece txt") {
                    dosyatürü = 3;
                }
                else if (dosyaCesidi.getSelectedItem() == "Sadece docx") {
                    dosyatürü = 4;
                }
                else if (dosyaCesidi.getSelectedItem() == "Sadece png") {
                    dosyatürü = 5;
                }

                //Kullanıcıdan aldıgımız bilgileri dosyaislemleri adlı fonksiyona parametre olarak gönderiyoruz
                try {
                    dosyaislem.dosyaislemleri(TasinacakDizin,HedefDizin, sifrele, gizle, dosyatürü);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null,"Hata oluştu: " + e.getMessage());
                }

            }
        });

        //Sifre çöz butonunu arayüze ekliyoruz
        JButton sifreCoz = new JButton("Şifre çöz");
        sifreCoz.setBounds(400, 180, 150, 50);
        jf.add(sifreCoz);

        sifreCoz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {

                JFileChooser jfc3 = new JFileChooser();
                jfc3.showOpenDialog(null);

                Dosyaİslemleri.sifreCozme(String.valueOf(jfc3.getSelectedFile()));



            }
        });

        //Arayüz islemlerini yapıyoruz.
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.setLayout(null);
        jf.setVisible(true);
        jf.setResizable(false);

    }

}




class Dosyaİslemleri extends Arayüz {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";
    public void dosyaislemleri(String kaynakDizin,String hedefDizin,int dosyaSifrele,int dosyaGizle,int dosyaTuru) throws IOException {

        // Kaynak ve hedef dizini temsil eden File nesnesini oluşturuyoruz
        File kaynakDizinFile = new File(kaynakDizin);
        File hedefDizinFile = new File(hedefDizin);

        // Kaynak dizini var mı diye kontrol ediyoruz
        if (kaynakDizinFile.exists() && kaynakDizinFile.isDirectory()) {

            // Hedef dizini var mı diye kontrol ediyoruz
            if(hedefDizinFile.exists() && hedefDizinFile.isDirectory()) {

                // Kaynak dizinindeki dosyaları listele
                File[] kaynakdosyalar = kaynakDizinFile.listFiles();

                // Dosyaları dönerek hedef dizine taşı
                if (kaynakdosyalar != null) {

                    //Kaynak klasörümüzde dosya olup olmadığını kontrol ediyoruz eğer yoksa belirtiyoruz
                    if (kaynakdosyalar.length != 0) {

                        //Dosya turlerinden dosyada bulunup bulunmadığını anlamak icin sayac olusturuyoruz.
                        int pdfsayac = 0;
                        int txtsayac = 0;
                        int docxsayac = 0;
                        int pngsayac = 0;

                        //Klasördeki her dosyaya bakmak için döngü oluşturuyoruz.
                        for (File dosya : kaynakdosyalar) {

                            //Dosyaya ulaşmak için klasörün adresine "/" ekleyip dosyanın ismini ekliyoruz.
                            File dosyaAdresi = new File(hedefDizin+"/"+dosya.getName());

                            //Dosyanın seçili uzantılarını döngü içinde kontrol ediyoruz.
                            if (dosya.isFile() && UzantiSecme(dosya.getName(), dosyaTuru)) {
                                try {
                                    Path kaynakDosyaYolu = dosya.toPath();
                                    Path hedefDosyaYolu = Path.of(hedefDizin, dosya.getName());

                                    // Dosyayı hedef dizine taşıma
                                    if (hedefDizinFile.exists() && hedefDizinFile.isDirectory()) {
                                        Files.move(kaynakDosyaYolu, hedefDosyaYolu, StandardCopyOption.REPLACE_EXISTING);


                                        //Seçili dosya türünden klasörde bulunuyor ise sayaçları arttırıyoruz.
                                        if (dosyaTuru == 2) {
                                            String uzanti = dosya.getName().substring(dosya.getName().lastIndexOf(".") + 1).toLowerCase();
                                            if (uzanti.equals("pdf")) {
                                                pdfsayac++;
                                            }
                                        }
                                        if (dosyaTuru == 3) {
                                            String uzanti = dosya.getName().substring(dosya.getName().lastIndexOf(".") + 1).toLowerCase();
                                            if (uzanti.equals("txt")) {
                                                txtsayac++;
                                            }
                                        }
                                        if (dosyaTuru == 4) {
                                            String uzanti = dosya.getName().substring(dosya.getName().lastIndexOf(".") + 1).toLowerCase();
                                            if (uzanti.equals("docx")) {
                                                docxsayac++;
                                            }
                                        }
                                        if (dosyaTuru == 5) {
                                            String uzanti = dosya.getName().substring(dosya.getName().lastIndexOf(".") + 1).toLowerCase();
                                            if (uzanti.equals("png")) {
                                                pngsayac++;
                                            }
                                        }

                                        JOptionPane.showMessageDialog(null, "Dosya taşındı: " + dosya.getName());
                                    }


                                    // Şifreleme seçeneği
                                    if (dosyaSifrele == 1) {
                                        try {
                                            String key = "0123456789abcdef"; // 128-bit anahtar

                                            // Dosya okuma ve yazma işlemleri için gerekli akışlar oluşturuluyor
                                            FileInputStream inputStream = new FileInputStream(dosyaAdresi);
                                            FileOutputStream outputStream = new FileOutputStream(dosyaAdresi+".LOCK");

                                            byte[] buffer = new byte[4096]; // Okuma ve şifreleme için kullanılacak tampon

                                            // Gizli anahtar oluşturuluyor
                                            Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);

                                            // Şifreleme işlemi için Cipher nesnesi oluşturuluyor
                                            Cipher cipher = Cipher.getInstance(TRANSFORMATION);

                                            // Şifreleme modunda Cipher nesnesi başlatılıyor
                                            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

                                            int bytesRead;
                                            while ((bytesRead = inputStream.read(buffer)) != -1) {
                                                // Tamponu şifreleyerek şifrelenmiş baytları elde ediyoruz
                                                byte[] encryptedBytes = cipher.update(buffer, 0, bytesRead);

                                                // Şifrelenmiş baytları dosyaya yazıyoruz
                                                outputStream.write(encryptedBytes);
                                            }

                                            // Şifreleme tamamlandığında Cipher nesnesi doFinal() metodu ile kalan baytları şifreleyerek elde ediyoruz
                                            byte[] encryptedBytes = cipher.doFinal();

                                            // Şifrelenmiş kalan baytları dosyaya yazıyoruz
                                            outputStream.write(encryptedBytes);

                                            // Akışları kapatıyoruz
                                            inputStream.close();
                                            outputStream.close();

                                            // Orijinal dosyanın silinmesi
                                            File originalFile = new File(String.valueOf(dosyaAdresi));
                                            originalFile.delete();


                                            JOptionPane.showMessageDialog(null,  dosya.getName() + " Şifrelendi" );

                                        } catch (Exception e) {
                                            JOptionPane.showMessageDialog(null,  dosya.getName() + " Şifrelenemiyor" );
                                        }


                                    }



                                    // Gizli dosya yapma seçeneği
                                    if (dosyaGizle == 1) {
                                        DosFileAttributeView dosFileAttributeView = Files.getFileAttributeView(dosyaAdresi.toPath(), DosFileAttributeView.class);
                                        if (dosFileAttributeView != null) {


                                            if (dosyaAdresi.isHidden()) {
                                                JOptionPane.showMessageDialog(null,  dosya.getName()+" Önceden gizlenmiş" );
                                            } else {
                                                dosFileAttributeView.setHidden(true);
                                                JOptionPane.showMessageDialog(null,  dosya.getName()+" Gizlendi" );
                                            }

                                        } else {
                                            JOptionPane.showMessageDialog(null,  dosya.getName()+" Gizlenemedi" );
                                        }

                                    }

                                } catch (IOException e) {
                                    JOptionPane.showMessageDialog(null, "Hata oluştu: " + e.getMessage());
                                }
                            }
                        }

                        //Seçili dosya türünün sayacını kontrol edip eğer yok ise bilgilendiriyoruz.
                        if (dosyaTuru == 2 && pdfsayac == 0) {
                            JOptionPane.showMessageDialog(null, "Dosyada pdf bulunmamaktadır.");
                        }
                        if (dosyaTuru == 3 && txtsayac == 0) {
                            JOptionPane.showMessageDialog(null, "Dosyada txt bulunmamaktadır.");
                        }
                        if (dosyaTuru == 4 && docxsayac == 0) {
                            JOptionPane.showMessageDialog(null, "Dosyada docx bulunmamaktadır.");
                        }
                        if (dosyaTuru == 5 && pngsayac == 0) {
                            JOptionPane.showMessageDialog(null, "Dosyada png bulunmamaktadır.");
                        }

                    }
                    //Dosya boş ise bilgilendiriyoruz
                    else {
                        JOptionPane.showMessageDialog(null, "Dosyada belge bulunmamaktadır.");
                    }
                }
            }
            //Hedef dizini geçersiz ise bilgilendiriyoruz
            else {
                JOptionPane.showMessageDialog(null, "Geçersiz hedef dizini.");
            }
        }
        //Kaynak dizini geçersiz ise bilgilendiriyoruz
        else {
            JOptionPane.showMessageDialog(null, "Geçersiz kaynak dizini.");
        }

    }

    // Desteklenen dosya uzantılarını kontrol eden bir yardımcı metot
    private static boolean UzantiSecme(String dosyaAdi,int dosyaSecim) {
        String uzanti = dosyaAdi.substring(dosyaAdi.lastIndexOf(".") + 1).toLowerCase();
        switch (dosyaSecim) {
            case 1:
                return uzanti.equals("pdf") || uzanti.equals("docx") || uzanti.equals("txt") || uzanti.equals("png");
            case 2:
                return uzanti.equals("pdf") ;
            case 3:
                return uzanti.equals("txt") ;
            case 4:
                return uzanti.equals("docx") ;
            case 5:
                return uzanti.equals("png") ;
        }
        return false;
    }

    //Şifre çözme işlemleri
    public static void sifreCozme(String sifrelidizin) {
        File sifreliDizinFile = new File(sifrelidizin);
        try {
            String key = "0123456789abcdef"; // 128-bit anahtar
            FileInputStream inputStream = new FileInputStream(sifrelidizin);
            FileOutputStream outputStream = new FileOutputStream(sifrelidizin.replace(".LOCK",""));
            byte[] buffer = new byte[4096];

            Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byte[] decryptedBytes = cipher.update(buffer, 0, bytesRead);
                outputStream.write(decryptedBytes);
            }

            byte[] decryptedBytes = cipher.doFinal();
            outputStream.write(decryptedBytes);

            inputStream.close();
            outputStream.close();


            // Şifrelenmiş dosyanın silinmesi
            File sifreliFile = new File(String.valueOf(sifreliDizinFile));
            sifreliFile.delete();

            JOptionPane.showMessageDialog(null,  sifreliDizinFile.getName() + " Çözüldü" );

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,  sifreliDizinFile.getName() + " Çözülemiyor" );
        }
    }

}

