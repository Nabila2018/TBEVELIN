const Event = require("../models/EventModel");
const EventParticipant = require("../models/EventParticipantModel");
const jwt = require("jsonwebtoken");
const { get, use } = require("../routes");
const pusher = require("../config/pusher.js");
const { Op } = require("sequelize");


function checkUserLoggedIn(req) {
  const authHeader = req.headers['authorization'];
  const accessToken = authHeader && authHeader.split(' ')[1];



  let user = null;
  if (accessToken) {
    try {
      const decoded = jwt.verify(
        accessToken,
        process.env.ACCESS_TOKEN_SECRET
      );
      user = {
        userId: decoded.userId,
      };

      console.log("User logged in:", user);
    } catch (error) {
      console.error("Token invalid or expired:", error.message);
      return { user: null };
    }
  }
  return { user };
}

const getEvents = async (req, res) => {
  try {
    const events = await Event.findAll();

    res.status(200).json({
      error: false,
      message: "Data events berhasil didapatkan",
      data: events,
    });
  } catch (error) {
    console.error("Error fetching events:", error.message);
    res.status(500).json({ error: true, message: "Internal server error" });
  }
};

const getEvent = async (req, res) => {
  try {
    const event = await Event.findByPk(req.params.id);
    const { user } = checkUserLoggedIn(req);

    const eventParticipants = await 
    EventParticipant.findAll({
      where: {
        eventId: req.params.id,userId: user.userId
      },
    });

    const isRegistered = eventParticipants.length ;
    console.log("isRegistered:", isRegistered);
    if (!event) {
      return res
        .status(404)
        .json({ error: true, message: "Event tidak ditemukan" });
    }

    res.status(200).json({
      error: false,
      message: "Data event berhasil didapatkan",
      data: { event, isRegistered }
    });
  } catch (error) {
    console.error("Error fetching event:", error.message);
    res.status(500).json({ error: true, message: "Internal server error" });
  }
};



const addEvent = async (req, res) => {
  try {
    const { user } = checkUserLoggedIn(req);

    if (!user) {
      return res
        .status(401)
        .json({ error: true, message: "User not authenticated" });
    }

    const { title, description, eventDate, location, category, speaker } =req.body;
    const posterUrl = req.file ? req.file.path : null;

    console.log("posterUrl:", posterUrl);
    console.log("Request Files:", req.files);
    console.log("Request Body:", req.body);
    if (
      !title ||
      !description ||
      !eventDate ||
      !location ||
      !category
    ) {
      return res.status(400).json({
        error: true,
        message: "All fields except 'poster' are required",
      });
    }

    if (!posterUrl) {
      return res.status(400).json({
        error: true,
        message: "Poster image is required",
      });
    }

    const event = await Event.create({
      userId: user.userId,
      title,
      description,
      eventDate,
      location,
      category,
      speaker,
      posterUrl,
    });

    res.status(201).json({
      error: false,
      message: "Event berhasil ditambahkan",
      data: event,
    });
  } catch (error) {
    console.error("Error creating event:", error.message);
    res.status(500).json({ error: true, message: "Internal server error" });
  }
};


const editEvent = async (req, res) => {
  try {
    const { user } = checkUserLoggedIn(req);

    if (!user) {
      return res
        .status(401)
        .json({ error: true, message: "User not authenticated" });
    }

    const userId = user.userId;
    const eventId = req.params.id;
    const { eventDate, location } = req.body;

    if (!eventDate && !location) {
      return res.status(400).json({
        error: true,
        message: "At least one of eventDate or location must be provided",
      });
    }

    console.log("Request body:", req.body);

    const event = await Event.findOne({
      where: {
        id: eventId,
        userId: userId,
      },
    });
    

    if (!event) {
      return res.status(404).json({ error: true, message: "Event not found" });
    }

    const oldEventDate = event.eventDate;

    // Create an object for the fields to update
    const updateData = {};
    if (eventDate) updateData.eventDate = eventDate;
    if (location) updateData.location = location;

    // Update the event with only the provided fields
    await event.update(updateData);

    if (eventDate && oldEventDate !== eventDate) {
      await pusherService.broadcastEventDateChange(eventId, oldEventDate, eventDate);
    }

   
    await pusherService.broadcastEventUpdate(eventId, updateData);

    

    res.status(200).json({
      error: false,
      message: "Event updated successfully",
      data: event,
    });
  } catch (error) {
    console.error("Error updating event:", error); // Log full error details
    res.status(500).json({ error: true, message: "Internal server error" });
  }
};


const regEvent = async (req, res) => {
  try {
    const { user } = checkUserLoggedIn(req);

    if (!user) {
      return res
        .status(401)
        .json({ error: true, message: "User not authenticated" });
    }

    const eventParticipant = await EventParticipant.create({
      userId: user.userId,
      eventId: req.params.id,
    });


    res.status(201).json({
      error: false,
      message: "Event berhasil ditambahkan",
      data: eventParticipant,
    });
  } catch (error) {
    console.error("Error creating event:", error.message);
    res.status(500).json({ error: true, message: "Internal server error" });
  }
};

const getMyEvents = async (req, res) => {
  try {
    const { user } = checkUserLoggedIn(req);

    if (!user) {
      return res
        .status(401)
        .json({ error: true, message: "User not authenticated" });
    }

    const events = await Event.findAll({
      where: {
        userId: user.userId
      }
    });

    res.status(200).json({
      error: false,
      message: "Events retrieved successfully",
      data: events
    });
  } catch (error) {
    console.error("Error fetching user's events:", error.message);
    res.status(500).json({ error: true, message: "Internal server error" });
  }
};

const getHistory = async (req, res) => {
  try {
    const { user } = checkUserLoggedIn(req);

    if (!user) {
      return res
        .status(401)
        .json({ error: true, message: "User not authenticated" });
    }

    const eventParticipants = await EventParticipant.findAll({
      where: { userId: user.userId },
      include: [
        {
          model: Event,
        },
      ],
    });



    res.status(200).json({
      error: false,
      message: "Events retrieved successfully",
      data: eventParticipants
    });
  } catch (error) {
    console.error("Error fetching user's events:", error.message);
    res.status(500).json({ error: true, message: "Internal server error" });
  }
};

const getMyEventsDetails = async (req, res) => {
  try {
    const userId = req.userId;
    const eventId = req.params.id;

    if (!userId) {
      return res
        .status(401)
        .json({ error: true, message: "User not authenticated" });
    }

    const event = await Event.findOne({
      where: {
        id: eventId,
        userId: userId
      }
    });

    if (!event) {
      return res.status(404).json({ error: true, message: "Event not found" });
    }

    res.status(200).json({
      error: false,
      message: "Event details retrieved successfully",
      data: event
    });
  } catch (error) {
    console.error("Error fetching event details:", error.message);
    res.status(500).json({ error: true, message: "Internal server error" });
  }
};

const searchEvent = async (req, res) => {
  try {
    const { title, description, eventDate, location, category, speaker } = req.query;

    const searchCriteria = {};

    if (title) {
      searchCriteria.title = { [Op.like]: `%${title}%` };
    }
    if (description) {
      searchCriteria.description = { [Op.like]: `%${description}%` };
    }
    if (eventDate) {
      searchCriteria.eventDate = eventDate;
    }
    if (location) {
      searchCriteria.location = { [Op.like]: `%${location}%` };
    }
    if (category) {
      searchCriteria.category = { [Op.like]: `%${category}%` };
    }
    if (speaker) {
      searchCriteria.speaker = { [Op.like]: `%${speaker}%` };
    }

    const events = await Event.findAll({
      where: searchCriteria
    });

    res.status(200).json({
      error: false,
      message: "Search results retrieved successfully",
      data: events
    });
  } catch (error) {
    console.error("Error searching events:", error.message);
    res.status(500).json({ error: true, message: "Internal server error" });
  }
};




module.exports = {
  getEvents,
  getEvent,
  addEvent,
  regEvent,
  getMyEvents,
  getMyEventsDetails,
  searchEvent,
  getHistory,
  editEvent
};
