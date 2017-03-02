package spammi.foorumi;

import spammi.foorumi.database.*;

public class Main {

    public static void main(String[] args) throws Exception {

        Database database = new Database("jdbc:sqlite:spammitietokanta.db");

        Spammifoorumi foorumi = new Spammifoorumi(database);
        foorumi.kaynnista();

    }
}
