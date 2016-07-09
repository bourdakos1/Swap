package com.xlythe.demo;

import java.util.Random;

/**
 * Created by Niko on 3/17/16.
 */
public class Message {

    String mBody;
    long mDate;
    boolean mIncoming;

    public Message(){
        switch (new Random().nextInt(9)){
            case 0:
                mBody = "This game is so fun";
                break;
            case 1:
                mBody = "OMG YEA IT ISSSS!!!!! Lets kill some people!!!!";
                break;
            case 2:
                mBody = "Sure";
                break;
            case 3:
                mBody = "Oh, lets kill Will";
                break;
            case 4:
                mBody = "Yessss lets kill will";
                break;
            case 5:
                mBody = "Oh yea definetly here is a long message of gibberish. To make things less boring. We should all vote on one person to kill. Any thoughts on who?";
                break;
            case 6:
                mBody = "Guys, I am NOT a member of the mafia.";
                break;
            case 7:
                mBody = "I dream of my next victim, so I have an alibi for killing that man";
                break;
            default:
                mBody = "Okay, lets just kill Sean, just because nobody likes him";
                break;
        }
        mDate = System.currentTimeMillis();
        mIncoming = new Random().nextInt(2) == 1;
    }

    public String getBody() {
        return mBody;
    }

    public long getTimestamp() {
        return mDate;
    }

    public boolean isIncoming() {
        return mIncoming;
    }
}
