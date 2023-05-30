//Eren Akkoç -  22120205045
//Rabia Ece Sert - 22120205057
//Azra Öykü Ulukan - 22120205059
//Dosya Taşıma,Şifreleme ve Gizleme Uygulaması

//Arayüz islemleri icin kütüphaneler
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//Dosya islemleri icin kütüphaneler
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.DosFileAttributes;

public class Main {
    public static void main(String[] args) {
        Arayüz a1 = new Arayüz();
        a1.ArayüzIslemleri();
    }
}




class Arayüz {
    public void ArayüzIslemleri(){
        Dosyaİslemleri dosyaislem=new Dosyaİslemleri();

        //Arayüzümüzü oluşturuyoruz.
        JFrame jf = new JFrame("KlasörAP");
        jf.setSize(600, 400);
        jf.setLocation(250, 250);

        //Ekrana yazdırma ve yazı alanı oluşturma işlemini yapıyoruz.
        JLabel TasınacakDizinLabel = new JLabel("Taşınacak Klasörün Konumu");
        JTextField TasınacakDizin = new JTextField(50);
        TasınacakDizin.setBounds(20, 40, 250, 30);
        TasınacakDizinLabel.setBounds(20, 21, 180, 20);
        jf.add(TasınacakDizinLabel);
        jf.add(TasınacakDizin);


        JLabel YeniKonumLabel = new JLabel("Yeni Konum");
        JTextField YeniKonum = new JTextField(50);
        YeniKonum.setBounds(20, 100, 250, 30);
        YeniKonumLabel.setBounds(20, 81, 180, 20);
        jf.add(YeniKonum);
        jf.add(YeniKonumLabel);

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

        //Taşı butonunu arayüze ekliyoruz
        JButton tası = new JButton("Taşı");
        tası.setBounds(350, 250, 150, 50);
        jf.add(tası);

        //Dosya türü seçeneğini arayüze ekliyoruz
        JLabel dosyaCesidiLabel = new JLabel("Dosya Türü");
        String[] dosyacesidi={"Tüm Dosyalar","Sadece txt","Sadece pdf","Sadece png","Sadece docx"};
        JComboBox dosyaCesidi = new JComboBox(dosyacesidi);
        dosyaCesidi.setBounds(360, 70, 130, 30);
        dosyaCesidiLabel.setBounds(360,45,130,30);
        jf.add(dosyaCesidiLabel);
        jf.add(dosyaCesidi);
        dosyaCesidi.setBackground(Color.LIGHT_GRAY);
        dosyaCesidi.setForeground(Color.WHITE);

        //Tası butonuna basıldığında yapılacak islemleri olusturuyoruz.
        tası.addActionListener(new ActionListener() {
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
                    dosyaislem.dosyaislemleri(TasınacakDizin.getText(), YeniKonum.getText(), sifrele, gizle, dosyatürü);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null,"Hata oluştu: " + e.getMessage());
                }

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

                                    // Şifreleme seçeneği
                                    if (dosyaSifrele == 1) {
                                        hedefDosyaYolu = Path.of(hedefDosyaYolu.toString() + ".encrypted");
                                        JOptionPane.showMessageDialog(null,  dosya.getName()+" Sifrelendi" );
                                    }

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

}

