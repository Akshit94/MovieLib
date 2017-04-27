package com.example.jainsaab.movielib.cast;

class Cast {

    private String actorName;
    private String characterName;
    private String profilePath;

    Cast(String actorName, String characterName, String profilePath){
        this.actorName = actorName;
        this.characterName = characterName;
        this.profilePath = profilePath;
    }

    String getActorName() {
        return actorName;
    }

    String getCharacterName() {
        return characterName;
    }

    String getProfilePath() {
        return profilePath;
    }
}
