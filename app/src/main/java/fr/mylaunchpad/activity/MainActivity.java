package fr.mylaunchpad.activity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.mylaunchpad.R;
import fr.mylaunchpad.bean.Sample;
import fr.mylaunchpad.controller.MusicController;
import fr.mylaunchpad.exception.TechnicalException;
import fr.mylaunchpad.tools.VolumeListener;

/**
 * Activité en charge de la lecture des différents samples en mode Touch Pad
 * <p>
 * Author: Jonathan B.
 * Created: 18/05/2018
 * Last Updated: 30/05/2018
 */
public class MainActivity extends AppCompatActivity implements OnClickListener, MediaPlayer.OnCompletionListener, OnSeekBarChangeListener {

    /**
     * Attributs
     */
    // Attributs de classe
    private MusicController musicCtrl;
    private List<Sample> listSample;
    private AudioManager audioManager = null;
    private VolumeListener volumeListener = null;

    // Attributs d'IHM
    private Button manageButton = null;
    private List<Button> listButton = null;
    private Button samplePlay1 = null;
    private Button samplePlay2 = null;
    private Button samplePlay3 = null;
    private Button samplePlay4 = null;
    private Button samplePlay5 = null;
    private Button samplePlay6 = null;
    private Button samplePlay7 = null;
    private Button samplePlay8 = null;
    private Button samplePlay9 = null;
    private Button samplePlay10 = null;
    private Button samplePlay11 = null;
    private Button samplePlay12 = null;
    private SeekBar volumeSeekbar = null;
    private ImageView volumeDown = null;
    private ImageView volumeUp = null;

    /**
     * Création de l'activité
     *
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String INFO = "[INFO]";
        Log.e(INFO, "=> MainActivity");
        // Initialisation du controller
        musicCtrl = new MusicController();
        // Initialisation du layout
        initLayout();
        // Initialise la liste des buttons
        initListButtons();
        // Initialisation des listeners
        initListeners();
    }

    /**
     * Démarrage de l'activité
     */
    @Override
    protected void onStart() {
        super.onStart();
        // Initialisation des MusicDataFiles
        initMusicDataFiles();
        // Initialisation des samples
        initSamples();
        // Initialisation de l'affichage
        initDisplay();
        // Initialisation de l'audio
        initAudio();
    }

    /**
     * Fermeture de l'activité
     */
    @Override
    protected void onStop() {
        super.onStop();
        // Ferme tous les MediaPlayers encore en cours
        closeAllMediaPlayer();
        // Fermeture de l'audio
        closeAudio();
    }

    /**
     * Initialise le layout et récupère les éléments
     */
    private void initLayout() {
        // Charge le layout
        setContentView(R.layout.activity_main);
        // Récupère les éléments du layout
        manageButton = findViewById(R.id.manageButton);
        samplePlay1 = findViewById(R.id.samplePlay1);
        samplePlay2 = findViewById(R.id.samplePlay2);
        samplePlay3 = findViewById(R.id.samplePlay3);
        samplePlay4 = findViewById(R.id.samplePlay4);
        samplePlay5 = findViewById(R.id.samplePlay5);
        samplePlay6 = findViewById(R.id.samplePlay6);
        samplePlay7 = findViewById(R.id.samplePlay7);
        samplePlay8 = findViewById(R.id.samplePlay8);
        samplePlay9 = findViewById(R.id.samplePlay9);
        samplePlay10 = findViewById(R.id.samplePlay10);
        samplePlay11 = findViewById(R.id.samplePlay11);
        samplePlay12 = findViewById(R.id.samplePlay12);
        volumeSeekbar = findViewById(R.id.volumeSeekbar);
        volumeDown = findViewById(R.id.volumeDown);
        volumeUp = findViewById(R.id.volumeUp);
    }

    /**
     * Initialise la liste des buttons
     */
    private void initListButtons() {
        listButton = new ArrayList<>();
        listButton.add(samplePlay1);
        listButton.add(samplePlay2);
        listButton.add(samplePlay3);
        listButton.add(samplePlay4);
        listButton.add(samplePlay5);
        listButton.add(samplePlay6);
        listButton.add(samplePlay7);
        listButton.add(samplePlay8);
        listButton.add(samplePlay9);
        listButton.add(samplePlay10);
        listButton.add(samplePlay11);
        listButton.add(samplePlay12);
    }

    /**
     * Initialise les listeners
     */
    private void initListeners() {
        // Bouton 'Manage'
        manageButton.setOnClickListener(this);
        // Toggle Buttons
        for (Button button : listButton) {
            if (button != null) {
                button.setOnClickListener(this);
            }
        }
        // Barre de volume
        volumeSeekbar.setOnSeekBarChangeListener(this);
        volumeDown.setOnClickListener(this);
        volumeUp.setOnClickListener(this);
    }

    /**
     * Initialisation les 2 MusicDataFiles (par défaut et personnalisé)
     */
    private void initMusicDataFiles() {
        try {
            // Initialisation des MusicDataFiles
            musicCtrl.initMusicDataFiles(this);
        } catch (TechnicalException tex) {
            Log.e("ERROR", "Impossible d'obtenir le MusicDataDefaultFile par défaut ou le MusicDataFile personnalisé !");
            Log.e("ERROR", "Fermeture de l'application !");
            // Ferme l'activité
            closeActivity();
        }
    }

    /**
     * Initialisation de la liste des 12 samples utilisés
     */
    private void initSamples() {
        listSample = musicCtrl.getSamples();
        if (listSample == null || listSample.size() != 12) {
            Log.e("ERROR", "Impossible de récupérer les 12 samples !");
            Log.e("ERROR", "Fermeture de l'application !");
            // Ferme l'activité
            closeActivity();
        }
    }

    /**
     * Initialisation de l'affichage
     */
    private void initDisplay() {
        // Pour chaque sample ..
        int i = 0;
        for (Sample sample : listSample) {
            // Affichage le nom sur le bouton associé
            listButton.get(i).setText(sample.getName());
            // Ré-initialise la couleur de la police par défaut
            listButton.get(i).setTextColor(getResources().getColor(R.color.colorAccent));
            i++;
        }
    }

    /**
     * Initialise l'Audio Manager, la SeekBar et le listener de changement de volume via touches physiques
     */
    private void initAudio() {
        try {
            // Définis le volume 'Media' du périphérique par défaut (au lieu de la sonnerie)
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            // Initialise l'Audio Manager
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            if (audioManager == null) {
                throw new TechnicalException("Impossible de récupérer l'Audio Manager !");
            }
            // Récupère le volume 'Media' du périphérique et le volume max
            int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            // Initialise la barre de réglage du volume
            volumeSeekbar.setProgress(volume);
            volumeSeekbar.setMax(maxVolume);
            // Initialise le listener de changement de volume via touches physiques
            volumeListener = new VolumeListener(new Handler(), audioManager, volumeSeekbar);
            // Enregistre le listener
            getApplicationContext().getContentResolver().registerContentObserver(android.provider.Settings.System.CONTENT_URI, true, volumeListener);
        } catch (Exception ex) {
            // Si erreur, ferme l'activité
            Log.e("ERROR", "Impossible d'initialiser l'audio !");
            // Ferme l'activité
            closeActivity();
        }
    }

    /**
     * Supprime le listener de changement de volume via touches physiques
     */
    private void closeAudio() {
        // Supprime l'enregistrement du listener de changement du volume via touches physiques
        getApplicationContext().getContentResolver().unregisterContentObserver(volumeListener);
    }

    /**
     * Lors du clic sur un bouton/image, traitement en fonction du bouton/image
     *
     * @param view View
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // Bouton 'Manage'
            case R.id.manageButton:
                // Ouvre l'activité 'Manage'
                Intent manageIntent = new Intent(MainActivity.this, ManageActivity.class);
                startActivity(manageIntent);
                break;
            case R.id.samplePlay1:
                // Lance le son en fonction du button
                turnOnOne(0);
                break;
            case R.id.samplePlay2:
                // Lance le son en fonction du button
                turnOnOne(1);
                break;
            case R.id.samplePlay3:
                // Lance le son en fonction du button
                turnOnOne(2);
                break;
            case R.id.samplePlay4:
                // Lance le son en fonction du button
                turnOnOne(3);
                break;
            case R.id.samplePlay5:
                // Lance le son en fonction du button
                turnOnOne(4);
                break;
            case R.id.samplePlay6:
                // Lance le son en fonction du button
                turnOnOne(5);
                break;
            case R.id.samplePlay7:
                // Lance le son en fonction du button
                turnOnOne(6);
                break;
            case R.id.samplePlay8:
                // Lance le son en fonction du button
                turnOnOne(7);
                break;
            case R.id.samplePlay9:
                // Lance le son en fonction du button
                turnOnOne(8);
                break;
            case R.id.samplePlay10:
                // Lance le son en fonction du button
                turnOnOne(9);
                break;
            case R.id.samplePlay11:
                // Lance le son en fonction du button
                turnOnOne(10);
                break;
            case R.id.samplePlay12:
                // Lance le son en fonction du button
                turnOnOne(11);
                break;
            // Boutons Volume +/-
            case R.id.volumeUp:
            case R.id.volumeDown:
                // Change le volume en fonction du bouton
                changeVolume(view.getId());
                break;
        }
    }

    /**
     * Active la lecture d'un sample
     *
     * @param selectedSample int
     */
    private void turnOnOne(int selectedSample) {
        // Récupère le sample à jouer
        Sample sample = listSample.get(selectedSample);

        // Initialise le Media Player en fonction du sample sélectionné
        MediaPlayer mp = musicCtrl.createMediaPlayer(this, sample);

        // Si Media Player correctement initialisé, lance la lecture
        if (mp != null) {
            // Initialise le listener de fin de lecture du Media Player
            mp.setOnCompletionListener(this);
            // Puis démarre la lecture
            mp.start();
            // Change la couleur de la police du bouton
            listButton.get(selectedSample).setTextColor(getResources().getColor(R.color.green));
            // Ajoute le Media Player utilisé au sample
            sample.getListMediaPlayer().add(mp);
        } else {
            // Affiche un message d'erreur
            Toast.makeText(this, R.string.play_fail, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Change le volume en fonction du bouton Volume + ou -
     *
     * @param viewId int
     */
    private void changeVolume(int viewId) {
        // Récupère le précédent volume 'Media' du périphérique
        int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int volumeMax = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        // Détermine le nouveau volume en fonction du bouton
        if (viewId == R.id.volumeUp) {
            // Si volume + demandé
            if (volume >= volumeMax) {
                volume = volumeMax;
            } else {
                volume++;
            }
        } else {
            // Si volume - demandé
            if (volume <= 0) {
                volume = 0;
            } else {
                volume--;
            }
        }
        // Met à jour le volume de l'Audio Manager et de la volumeSeekBar
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_PLAY_SOUND);
        volumeSeekbar.setProgress(volume);
    }

    /**
     * Lors de la fin de lecture d'un Media Player, remet la couleur de la police par défaut si besoin, puis ferme le Media Player
     *
     * @param currentMp MediaPlayer
     */
    @Override
    public void onCompletion(MediaPlayer currentMp) {
        // Vérifie si besoin de remettre la couleur de police d'un bouton par défaut
        int i = 0;
        for (Sample sample : listSample) {
            List<MediaPlayer> listMp = sample.getListMediaPlayer();
            // Utilisation d'un itérator pour pouvoir supprimer un élément d'une liste
            // pendant son parcours sans générer de ConcurrentModificationException
            Iterator<MediaPlayer> iter = listMp.iterator();
            while (iter.hasNext()) {
                MediaPlayer mp = iter.next();
                if (mp == currentMp)
                    // Supprime le MediaPlayer de la liste
                    iter.remove();
                // Vérifie si il reste des MediaPlayers encore en lecture pour ce sample
                if (listMp.size() == 0) {
                    // Remet la couleur par défaut de la police du bouton
                    listButton.get(i).setTextColor(getResources().getColor(R.color.colorAccent));
                }
            }
            i++;
        }
        // Ferme le media player utilisé
        closeMediaPlayer(currentMp);
    }

    /**
     * Ferme le MediaPlayer
     *
     * @param mp MediaPlayer
     */
    private void closeMediaPlayer(MediaPlayer mp) {
        if (mp != null) {
            mp.stop();
            mp.release();
        }
    }

    /**
     * Ferme tous les MediaPlayers en parcourant la liste des samples
     */
    private void closeAllMediaPlayer() {
        // Pour tous les samples possédants des MediaPlayers enregistrés
        for (Sample sample : listSample) {
            if (sample.getListMediaPlayer() != null && sample.getListMediaPlayer().size() > 0) {
                // Ferme tous les MediaPlayers
                for (MediaPlayer mp : sample.getListMediaPlayer()) {
                    closeMediaPlayer(mp);
                }
                // Ré-initialise la liste des MediaPlayers
                sample.resetListMediaPlayer();
            }
        }
    }

    /**
     * Lors du changement du volume via la SeekBar
     *
     * @param seekBar  SeekBar
     * @param progress int
     * @param fromUser boolean
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // Met à jour l'Audio Manager en fonction de la progression de la SeekBar
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
        Log.e("INFO", "Volume : " + String.valueOf(progress));
    }

    /**
     * Inutilisé
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    /**
     * Ferme l'activité
     */
    private void closeActivity() {
        finish();
    }

}
