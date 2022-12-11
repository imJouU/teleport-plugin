package me.jouu.teleport.Managers;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RequestManager {
    private static final List<Request> requests = new ArrayList<>();

    public static void addNewRequest(Request request) {
        requests.add(request);
    }

    public static void deleteRequest(Request request) {
        requests.remove(request);
    }

    public static Request findReqById(UUID reqId) {
        for (Request request : requests) if (request.getUuid().equals(reqId)) return request;

        return null;
    }

    public static Request findReqByRequesterAndTarget(Player requester, Player target) {
        for (Request request : requests) if (request.getRequester() == requester && request.getTarget() == target) return request;

        return null;
    }
}
