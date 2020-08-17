package de.miguel.frozzenlist.codemycode;

public class Quote
{
    private final String quoteText;
    private final String quoteAuthor;
    private final int imageID;

    public Quote(String quoteText,String quoteAuthor,int imageID){
        this.quoteText=quoteText;
        this.quoteAuthor=quoteAuthor;
        this.imageID=imageID;
    }
    public String getQuoteText(){
        return quoteText;
    }
    public String getQuoteAuthor(){
        return quoteAuthor;
    }
    public int getImageID(){
        return imageID;
    }

    @Override
    public String toString() {
        return  quoteText + " - " + quoteAuthor + " - ID: " + imageID;

    }
}
