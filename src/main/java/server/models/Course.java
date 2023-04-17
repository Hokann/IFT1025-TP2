package server.models;

import java.io.Serializable;

/**
 * Classe qui définit un cours quelconque. Un cours a 3 attributs: un nom, un code, et une session associée.
 * De plus, cette classe définit plusieurs getter & setters pour récupérer et assigner un nom, code, ou session.
 */
public class Course implements Serializable {

    private String name;
    private String code;
    private String session;

    /**
     * Méthode qui construit un objet de type Course. C'est le constructeur de la classe qui renvoie un nom, un code et
     * une session d'un cours.
     *
     * @param name nom du cours
     * @param code code du cours
     * @param session session à laquelle le cours est donné
     */
    public Course(String name, String code, String session) {
        this.name = name;
        this.code = code;
        this.session = session;
    }

    /**
     * Getter pour récupérer le nom d'un cours.
     * @return le nom du cours
     */
    public String getName() {
        return name;
    }

    /**
     * Setter pour assigner un nom à un cours.
     * @param name le nom du cours
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter pour récupérer le code d'un cours.
     * @return le code du cours
     */
    public String getCode() {
        return code;
    }

    /**
     * Setter pour assigner un code à un cours.
     * @param code le code du cours
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Getter pour récupérer la session d'un cours.
     * @return la session du cours
     */
    public String getSession() {
        return session;
    }

    /**
     * Setter pour assigner une session à un cours.
     * @param session la session du cours
     */
    public void setSession(String session) {
        this.session = session;
    }

    /**
     * Méthode qui l'ensemble nom, code et session d'un cours en un string facilement déchiffrable.
     * @return le string.
     */
    @Override
    public String toString() {
        return "Course{" +
                "name=" + name +
                ", code=" + code +
                ", session=" + session +
                '}';
    }
}
