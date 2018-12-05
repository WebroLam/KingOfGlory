package com.jerry.mapObjects.heroes;

import com.jerry.glory.GUI;
import com.jerry.mapObjects.Location;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;


/**
 * @author jerry
 */
public class automatic extends Hero {
    protected static final Logger logger = LogManager.getLogger();

    public automatic(JSONObject obj) {
        super(obj);
    }
    public automatic(JSONObject obj,GUI game) {
        super(obj,game);
        logger.debug("Automatic generated. name = " + this.name);
    }



}
