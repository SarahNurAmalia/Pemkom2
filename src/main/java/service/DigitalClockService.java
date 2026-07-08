/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.swing.JLabel;

/**
 *
 * @author ASUS
 */
public class DigitalClockService {
    private final JLabel targetLabel;
    private final String pattern;

    public DigitalClockService(JLabel targetLabel, String pattern) {
        this.targetLabel = targetLabel;
        this.pattern = pattern;
    }

    /**
     * Menyiapkan objek Thread tanpa langsung menjalankannya.
     * 
     * @return Objek Thread dalam fase 'New' [3].
     */
    public Thread getThread() {
        Runnable clockTask = () -> {
    try {
        while (!Thread.currentThread().isInterrupted()) {

            DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(pattern, I18nService.getCurrentLocale());

            LocalDateTime now = LocalDateTime.now();
            String timeFormatted = now.format(formatter);

            targetLabel.setText(timeFormatted);

            Thread.sleep(1000);
        }
    } catch (InterruptedException e) {
        System.out.println(Thread.currentThread().getName() + " dihentikan.");
    }
};

        return new Thread(clockTask);
    }

}
