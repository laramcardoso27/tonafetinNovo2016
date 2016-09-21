package br.com.cp2ejr.tonafetin2016.Models;

/**
 * Created by Lara on 17/08/2016.
 */
public class Project {

        private int id;         //Project ID.
        private int position;   //Project position.
        private String name;    //project name.
        private int votes;      //Total project votes.

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getVotes() {
            return votes;
        }

        public void setVotes(int votes) {
            this.votes = votes;
        }
}
