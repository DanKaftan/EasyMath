package com.dan.kaftan.game.targil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BankOfTargils {

    protected List<Targil> targlilim = new ArrayList<Targil>();
    protected List<Targil> doneTarglilim = new ArrayList<Targil>();

    protected Random rand = new Random();

    public void setTarglilim (List<Targil> targlilim){
        this.targlilim = targlilim;
    }

    public List<Targil> getTarglilim (){
        return targlilim;
    }

    public Targil getRandomTargil () {
        int location = rand.nextInt(targlilim.size());
        return targlilim.get(location);
    }

    public Targil removeRandomTargil () {
        int location = rand.nextInt(targlilim.size());
        System.out.println("Targilim remains: " +targlilim.size());
        Targil targil = targlilim.remove(location);

        // keep on the side to be use again later
        doneTarglilim.add(targil);

        // if we done all the targilimi, take the ones we did already and return them back to the list
        if (targlilim.size()==0){
            targlilim.addAll(doneTarglilim);
            doneTarglilim.clear();
        }
        return targil;
    }

}
