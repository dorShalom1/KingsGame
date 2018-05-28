package com.example.dshalom.kingsgame;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    final int[] kings = {0, 3, 12, 15};
    final int[] queens = {1, 2, 13, 14};
    final int[] jacks = {4, 7, 8, 11};
    final String[] defaultImages = {"k", "q", "q", "k", "j", "e", "e", "j", "j", "e", "e", "j", "k", "q", "q", "k"};
    final String[] shapes = {"c", "d", "h", "s"};
    ImageView deckSrc;
    ImageView deckDest;
    ImageView newCard;
    TextView cardsLeft;
    TextView cardsRem;
    MediaPlayer s_new_card;
    MediaPlayer s_new_game;
    MediaPlayer s_place_found;
    MediaPlayer s_remove_passed;
    AdView mAdView;
    Button newGame;
    Button undo;
    boolean stateAdding;
    boolean newCardAvailable;
    boolean tableFull;
    boolean canRemove;
    ArrayList<String> cardsSrc;
    ArrayList<String> cardsDest;
    ArrayList<Place> places;
    ArrayList<GameStep> steps;
    ArrayList<String> names;

    int newCardId;
    int numOfCardsInDeck;
    int numOfCardsRemoved;
    int numOfHighlighted;

    String addCard;
    String removeCard1;
    String removeCard2;
    String removeCard3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        stateAdding = true;
        deckSrc = findViewById(R.id.deckSrc);
        deckDest = findViewById(R.id.deckDest);
        newCard = findViewById(R.id.newCard);

        newGame = findViewById(R.id.newGame);
        undo = findViewById(R.id.undo);

        cardsLeft = findViewById(R.id.cardsLeft);
        cardsRem = findViewById(R.id.cardsRem);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        s_new_card = MediaPlayer.create(this, R.raw.new_card);
        s_new_game = MediaPlayer.create(this, R.raw.new_game);
        s_place_found = MediaPlayer.create(this, R.raw.place_found);
        s_remove_passed = MediaPlayer.create(this, R.raw.remove_passed);

        places = new ArrayList<>();
        places.add(new Place((ImageView) findViewById(R.id.place1), 0));
        places.add(new Place((ImageView) findViewById(R.id.place2), 1));
        places.add(new Place((ImageView) findViewById(R.id.place3), 2));
        places.add(new Place((ImageView) findViewById(R.id.place4), 3));
        places.add(new Place((ImageView) findViewById(R.id.place5), 4));
        places.add(new Place((ImageView) findViewById(R.id.place6), 5));
        places.add(new Place((ImageView) findViewById(R.id.place7), 6));
        places.add(new Place((ImageView) findViewById(R.id.place8), 7));
        places.add(new Place((ImageView) findViewById(R.id.place9), 8));
        places.add(new Place((ImageView) findViewById(R.id.place10), 9));
        places.add(new Place((ImageView) findViewById(R.id.place11), 10));
        places.add(new Place((ImageView) findViewById(R.id.place12), 11));
        places.add(new Place((ImageView) findViewById(R.id.place13), 12));
        places.add(new Place((ImageView) findViewById(R.id.place14), 13));
        places.add(new Place((ImageView) findViewById(R.id.place15), 14));
        places.add(new Place((ImageView) findViewById(R.id.place16), 15));

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        initCards();
        startClickListeners();
    }

    private void startClickListeners() {
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewGame();
            }
        });

        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleUndo();
            }
        });

        deckSrc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stateAdding) {
                    handleAddingState();
                } else {
                    updateBoardFull();
                    if (tableFull) {
                        if (checkIfRemoveValid())
                            Toast.makeText(getApplicationContext(), "The Board is Full!\nRemove Cards", Toast.LENGTH_SHORT).show();
                    } else {
                        stateAdding = true;
                        steps.add(new GameStep(GameStep.Type.CHANGE_STATE));
                        handleAddingState();
                    }
                }
            }
        });

        deckDest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRemoveState();
            }
        });

        for (final Place place : places) {
            place.getImage().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (stateAdding)
                        handleAddingPlace(place);
                    else
                        handleRemovePlace(place);
                }
            });
        }
    }

    private void stopClickLiteners() {
        deckSrc.setOnClickListener(null);
        deckDest.setOnClickListener(null);
        for (final Place place : places) {
            place.getImage().setOnClickListener(null);
        }
    }

    private boolean checkIfRemoveValid() {
        String card1, card2;
        for (Place place1 : places) {
            card1 = getResources().getResourceEntryName(place1.getImageId());
            if (card1.contains("j") || card1.contains("q") || card1.contains("k"))
                continue;
            if (card1.contains("10"))
                return true;
            for (Place place2 : places) {
                card2 = getResources().getResourceEntryName(place2.getImageId());
                if (card2.contains("j") || card2.contains("q") || card2.contains("k") || card2.equals(card1))
                    continue;
                if (isRemovable(card1, card2))
                    return true;
            }
        }
        return false;
    }

    private boolean checkWin() {
        for (String card : cardsSrc) {
            if (card.contains("j") || card.contains("q") || card.contains("k"))
                return false;
        }
        return true;
    }

    private void updateBoardFull() {
        for (Place place : places) {
            if (!place.isOccupied())
                tableFull = false;
        }
        if (tableFull) {
            if(!checkIfRemoveValid())
                gameOverDialog();
            else {
                stateAdding = false;
                steps.add(new GameStep(GameStep.Type.CHANGE_STATE));
            }
        }
    }

    private void startNewGame() {
        numOfHighlighted = 0;
        removeCard1 = "";
        removeCard2 = "";
        stateAdding = true;
        tableFull = false;
        newCardAvailable = false;
        dehighlight();
        initImages();
        initCards();
        startClickListeners();
        s_new_game.start();
    }

    private void handleUndo() {
        if (steps.size() > 0) {
            GameStep step = steps.get(steps.size() - 1);
            dehighlight();
            if (newCardAvailable) {
                newCard.setImageResource(R.drawable.empty);
                newCardAvailable = false;
                cardsSrc.add(addCard);
                numOfCardsInDeck++;
                cardsLeft.setText(numOfCardsInDeck + "\n Cards");
                return;
            }
            steps.remove(steps.size() - 1);
            switch (step.getType()) {
                case CHANGE_STATE:
                    stateAdding = !stateAdding;
                    handleUndo();
                    break;
                case ADD:
                    cardsSrc.add(step.getCard());
                    numOfCardsInDeck++;
                    cardsLeft.setText(numOfCardsInDeck + "\n Cards");
                    setDefaultImage(step.getPlace());
                    places.get(step.getPlace()).setOccupied(false);
                    break;
                case REMOVE:
                    numOfCardsRemoved--;
                    cardsDest.remove(numOfCardsRemoved);
                    cardsRem.setText(numOfCardsRemoved + "\n Cards");
                    places.get(step.getPlace()).setOccupied(true);
                    places.get(step.getPlace()).setImage(getResources().getIdentifier(step.getCard(), "drawable", getPackageName()));
                    if (!step.getCard().contains("10")) {
                        if (removeCard3 != null) {
                            removeCard3 = null;
                        } else {
                            removeCard3 = step.getCard();
                            handleUndo();
                        }
                    }
                    break;
            }
        }
    }

    private void handleAddingPlace(Place place) {
        if (place.isHighlighted()) {
            place.setImage(newCardId);
            place.setOccupied(true);
            newCard.setImageResource(R.drawable.empty);
            newCardAvailable = false;
            s_place_found.start();
            steps.add(new GameStep(GameStep.Type.ADD, place.getIndex(), addCard));
            dehighlight();
            updateBoardFull();
        }
        if (checkWin())
            youWonDialog();
    }

    private void handleRemoveState() {
        if (canRemove) {
            if (removeCard1.contains("10"))
                moveToDest(removeCard1);
            else {
                moveToDest(removeCard1);
                moveToDest(removeCard2);
            }
            s_remove_passed.start();
            deckDest.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            canRemove = false;
        }
    }

    private void moveToDest(String card) {
        for (int i = 0; i < places.size(); i++) {
            if (card.equals(getResources().getResourceEntryName(places.get(i).getImageId()))) {
                places.get(i).setOccupied(false);
                places.get(i).setHighlighted(false);
                setDefaultImage(i);
                steps.add(new GameStep(GameStep.Type.REMOVE, places.get(i).getIndex(), card));
                cardsDest.add(card);
                numOfCardsRemoved++;
                cardsRem.setText(numOfCardsRemoved + "\n Cards");
                return;
            }
        }
    }

    private void setDefaultImage(int placeId) {
        switch (defaultImages[placeId]) {
            case "j":
                places.get(placeId).setImage(R.drawable.empty_jack);
                break;
            case "k":
                places.get(placeId).setImage(R.drawable.empty_king);
                break;
            case "q":
                places.get(placeId).setImage(R.drawable.empty_queen);
                break;
            case "e":
                places.get(placeId).setImage(R.drawable.empty);
                break;
        }
    }

    private void handleRemovePlace(Place place) {
        updateNumOfHighlighted();
        if (place.isOccupied()) {
            switch (numOfHighlighted) {
                case 0:
                    removeCard1 = getResources().getResourceEntryName(place.getImageId());
                    if (removeCard1.contains("j") || removeCard1.contains("q") || removeCard1.contains("k")) {
                        Toast.makeText(getApplicationContext(), "Don't Touch Royalty!", Toast.LENGTH_SHORT).show();
                        removeCard1 = "";
                        break;
                    }
                    place.setHighlighted(true);
                    if (removeCard1.contains("10")) {
                        deckDest.setBackgroundDrawable(new ColorDrawable(Color.YELLOW));
                        canRemove = true;
                    }
                    break;
                case 1:
                    if (place.isHighlighted()) {
                        place.setHighlighted(false);
                        if (removeCard1.contains("10"))
                            deckDest.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        removeCard1 = "";
                        canRemove = false;
                    } else if (removeCard1.contains("10"))
                        Toast.makeText(getApplicationContext(), "Click The Deck to Remove Card.", Toast.LENGTH_SHORT).show();
                    else {
                        removeCard2 = getResources().getResourceEntryName(place.getImageId());
                        if (removeCard2.contains("j") || removeCard2.contains("q") || removeCard2.contains("k")) {
                            Toast.makeText(getApplicationContext(), "Don't Touch Royalty!", Toast.LENGTH_SHORT).show();
                            removeCard2 = "";
                            break;
                        }
                        if (isRemovable(removeCard1, removeCard2)) {
                            place.setHighlighted(true);
                            deckDest.setBackgroundDrawable(new ColorDrawable(Color.YELLOW));
                            canRemove = true;
                        } else {
                            removeCard2 = "";
                            Toast.makeText(getApplicationContext(), "Sum Must By 10", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case 2:
                    if (place.isHighlighted()) {
                        place.setHighlighted(false);
                        if (removeCard1.equals(getResources().getResourceEntryName(place.getImageId()))) {
                            removeCard1 = removeCard2;
                        }
                        removeCard2 = "";
                        deckDest.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        canRemove = false;
                    } else
                        Toast.makeText(getApplicationContext(), "Click The Deck to Remove Card.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private boolean isRemovable(String card1, String card2) {
        int num1 = Integer.parseInt(card1.substring(1));
        int num2 = Integer.parseInt(card2.substring(1));
        return num1 + num2 == 10;
    }

    private void handleNewCard() {
        boolean openPlaces = false;
        tableFull = true;
        highlight(addCard);
        for (Place place : places) {
            if (place.isHighlighted())
                openPlaces = true;
        }
        if (!openPlaces) {
            gameOverDialog();
        }
    }

    private void handleAddingState() {
        if (!newCardAvailable) {
            numOfCardsInDeck--;
            cardsLeft.setText(numOfCardsInDeck + "\n Cards");
            s_new_card.start();
            newCardId = getResources().getIdentifier(cardsSrc.get(numOfCardsInDeck), "drawable", getPackageName());
            addCard = cardsSrc.get(numOfCardsInDeck);
            cardsSrc.remove(numOfCardsInDeck);
            newCard.setImageResource(newCardId);
            newCardAvailable = true;
            handleNewCard();
        }
    }

    private void updateNumOfHighlighted() {
        numOfHighlighted = 0;
        for (Place place : places) {
            if (place.isHighlighted())
                numOfHighlighted++;
        }
    }

    private void initImages() {
        for (int i = 0; i < places.size(); i++) {
            places.get(i).setOccupied(false);
            setDefaultImage(i);
        }
        newCard.setImageResource(R.drawable.empty);
    }

    private void dehighlight() {
        newCard.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        for (Place place : places)
            place.setHighlighted(false);
    }

    private void highlight(String currentCard) {
        newCard.setBackgroundDrawable(new ColorDrawable(Color.YELLOW));
        if (currentCard.contains("j")) {
            if (!places.get(4).isOccupied()) {
                places.get(4).setHighlighted(true);
            }
            if (!places.get(7).isOccupied()) {
                places.get(7).setHighlighted(true);
            }
            if (!places.get(8).isOccupied()) {
                places.get(8).setHighlighted(true);
            }
            if (!places.get(11).isOccupied()) {
                places.get(11).setHighlighted(true);
            }
        } else if (currentCard.contains("q")) {
            if (!places.get(1).isOccupied()) {
                places.get(1).setHighlighted(true);
            }
            if (!places.get(2).isOccupied()) {
                places.get(2).setHighlighted(true);
            }
            if (!places.get(13).isOccupied()) {
                places.get(13).setHighlighted(true);
            }
            if (!places.get(14).isOccupied()) {
                places.get(14).setHighlighted(true);
            }
        } else if (currentCard.contains("k")) {
            if (!places.get(0).isOccupied()) {
                places.get(0).setHighlighted(true);
            }
            if (!places.get(3).isOccupied()) {
                places.get(3).setHighlighted(true);
            }
            if (!places.get(12).isOccupied()) {
                places.get(12).setHighlighted(true);
            }
            if (!places.get(15).isOccupied()) {
                places.get(15).setHighlighted(true);
            }
        } else {
            for (Place place : places) {
                if (!place.isOccupied()) {
                    place.setHighlighted(true);
                }
            }
        }
    }

    private void initCards() {
        initNames();
        steps = new ArrayList<>();
        numOfCardsInDeck = names.size() * 4;
        numOfCardsRemoved = 0;
        cardsLeft.setText(numOfCardsInDeck + "\n Cards");
        cardsRem.setText("0\n Cards");
        cardsDest = new ArrayList<>();
        cardsSrc = new ArrayList<>();
        for (String shape : shapes) {
            for (String name : names) {
                cardsSrc.add(shape + name);
            }
        }
        Collections.shuffle(cardsSrc);
    }

    private void initNames(){
        names = new ArrayList<>();
        names.add("1");
        names.add("9");
        names.add("10");
        names.add("j");
        names.add("q");
        names.add("k");
        SharedPreferences pref = getSharedPreferences("dor", MODE_PRIVATE);
        int level = pref.getInt("level", 3);
        if(level >= 1){
            names.add("2");
            names.add("8");
        }
        if(level >= 2){
            names.add("3");
            names.add("7");
        }
        if(level == 3){
            names.add("4");
            names.add("5");
            names.add("6");
        }
    }

    public void youWonDialog() {
        ImageView crown = new ImageView(this);
        crown.setImageResource(R.drawable.crown);
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("OMG you finished!!!")
                .setView(crown)
                .setCancelable(false)
                .setPositiveButton("New Game", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startNewGame();
                    }
                });
        // Create the AlertDialog object and return it
        AlertDialog dialog = builder.create();
        dialog.show();
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        LinearLayout parent = (LinearLayout) positiveButton.getParent();
        parent.setGravity(Gravity.CENTER_HORIZONTAL);
        View leftSpacer = parent.getChildAt(1);
        leftSpacer.setVisibility(View.GONE);
    }

    public void gameOverDialog() {
        // Use the Builder class for convenient dialog construction
        // stopClickLiteners();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Game Over!")
                .setCancelable(false)
                .setPositiveButton("New Game", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startNewGame();
                    }
                })
                .setNegativeButton("Resume", null);
        // Create the AlertDialog object and return it
        AlertDialog dialog = builder.create();
        dialog.show();
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        LinearLayout parent = (LinearLayout) positiveButton.getParent();
        parent.setGravity(Gravity.CENTER_HORIZONTAL);
        View leftSpacer = parent.getChildAt(1);
        leftSpacer.setVisibility(View.GONE);
    }
}