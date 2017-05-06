package ProgramsPorblems;
import java.util.HashMap;

class Movie {
    private String name, actor;
    private int releaseYr;
    
    @Override
      public boolean equals(Object o) {
       Movie m = (Movie) o;
       return m.actor.equals(this.actor) && m.name.equals(this.name) && m.releaseYr == this.releaseYr;
    
        }
      
        @Override
    
        public int hashCode() {
                return actor.hashCode() + name.hashCode() + releaseYr;
           }

    public String getName() {

        return name;
    }
    public void setName(String name) {

        this.name = name;

    }
    public String getActor() {

        return actor;

    }
    public void setActor(String actor) {
        this.actor = actor;    }

    public int getReleaseYr() {
        return releaseYr;

    }
    public void setReleaseYr(int releaseYr) {
        this.releaseYr = releaseYr;
    }


}
 
