package com.thimat.sockettelkarnet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ListagentModel {
    @SerializedName("agent")
    @Expose
    ArrayList<AgentModel> agentModels = new ArrayList<AgentModel>();

    //-------------------------------------------------
    public ArrayList<AgentModel> getAgentModels() {
        return agentModels;
    }

    public void setAgentModels(ArrayList<AgentModel> agentModels) {
        this.agentModels = agentModels;
    }
}
