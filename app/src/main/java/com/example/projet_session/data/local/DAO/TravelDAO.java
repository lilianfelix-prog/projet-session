package com.example.projet_session.data.local.DAO;

import com.example.projet_session.data.local.entity.Travel;


import java.util.ArrayList;
import java.util.List;

public class TravelDAO {
    private List<Travel> travelList = new ArrayList<>();

    public TravelDAO(){

    }

    public List<Travel> getTravels(){
        return travelList;
    }

    public void addTravels(List<Travel> travels){
        travelList.addAll(travels);
    }

    public void clearTravels(){
        travelList.clear();
    }
}
