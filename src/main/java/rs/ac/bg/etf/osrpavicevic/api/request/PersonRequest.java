package rs.ac.bg.etf.osrpavicevic.api.request;

import rs.ac.bg.etf.osrpavicevic.constants.TypeOfPersons;

public record PersonRequest (
        TypeOfPersons type,
        String firstname,
        String lastname,
        String position
){

}