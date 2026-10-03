import { useEffect, useState } from "react";
import "./App.css";
import { getEvents, loginUser } from "./api";

function App() {

    const [events, setEvents] = useState([]);

    const [loading, setLoading] = useState(false);

    const [error, setError] = useState("");

    const [showLogin, setShowLogin] = useState(false);

    const [showEventDetails, setShowEventDetails] = useState(false);

    const [selectedEvent, setSelectedEvent] = useState(null);

    const [collegeEmail, setCollegeEmail] = useState("");

    const [password, setPassword] = useState("");

    const [loggedIn, setLoggedIn] = useState(
        Boolean(localStorage.getItem("token"))
    );


    useEffect(() => {

        const token = localStorage.getItem("token");

        if (!token) {
            return;
        }

        loadEvents(token);

    }, []);


    async function loadEvents(token) {

        try {

            setLoading(true);

            setError("");

            const data = await getEvents(token);

            setEvents(data);

        } catch (err) {

            console.error(err);

            localStorage.removeItem("token");

            setLoggedIn(false);

            setError("Session expired. Please login again.");

        } finally {

            setLoading(false);

        }
    }


    async function handleLogin(event) {

        event.preventDefault();

        try {

            setLoading(true);

            setError("");

            const data = await loginUser(
                collegeEmail,
                password
            );

            /*
             * Store the JWT returned by Spring Boot.
             */
            localStorage.setItem(
                "token",
                data.token
            );

            setLoggedIn(true);

            setShowLogin(false);

            setPassword("");

            await loadEvents(data.token);

        } catch (err) {

            console.error(err);

            setError(
                err.message || "Login failed"
            );

        } finally {

            setLoading(false);

        }
    }


    function handleLogout() {

        localStorage.removeItem("token");

        setLoggedIn(false);

        setEvents([]);

        setSelectedEvent(null);

        setShowEventDetails(false);

    }


    function handleViewEvent(event) {

        setSelectedEvent(event);

        setShowEventDetails(true);

    }


    function closeEventDetails() {

        setShowEventDetails(false);

        setSelectedEvent(null);

    }


    return (
        <div className="app">

            {/* =========================
                NAVBAR
            ========================= */}

            <nav className="navbar">

                <div className="brand">

                    <div className="brand-logo">
                        G
                    </div>

                    <div>

                        <h1>
                            GECCS
                        </h1>

                        <span>
                            Event Management
                        </span>

                    </div>

                </div>


                <div className="nav-links">

                    <a href="#home">
                        Home
                    </a>

                    <a href="#events">
                        Events
                    </a>

                    <a href="#about">
                        About
                    </a>


                    {!loggedIn ? (

                        <button
                            className="login-button"
                            onClick={() => {
                                setShowLogin(true);
                                setError("");
                            }}
                        >
                            Login
                        </button>

                    ) : (

                        <button
                            className="login-button"
                            onClick={handleLogout}
                        >
                            Logout
                        </button>

                    )}

                </div>

            </nav>


            {/* =========================
                HERO
            ========================= */}

            <main>

                <section
                    className="hero"
                    id="home"
                >

                    <div className="hero-content">

                        <p className="eyebrow">
                            GOVERNMENT COLLEGE OF ENGINEERING
                        </p>

                        <h2>
                            Discover.
                            <br />
                            Participate.
                            <br />
                            <span>
                                Experience.
                            </span>
                        </h2>

                        <p className="hero-description">
                            One platform for discovering college events,
                            registering for activities, and staying connected
                            with the GECCS community.
                        </p>

                        <div className="hero-actions">

                            <button
                                className="primary-button"
                                onClick={() =>
                                    document
                                        .getElementById("events")
                                        ?.scrollIntoView()
                                }
                            >
                                Explore Events
                            </button>

                            <button
                                className="secondary-button"
                                onClick={() =>
                                    document
                                        .getElementById("about")
                                        ?.scrollIntoView()
                                }
                            >
                                Learn More
                            </button>

                        </div>

                    </div>


                    {/* =========================
                        UPCOMING EVENT
                    ========================= */}

                    <div className="hero-card">

                        <div className="card-top">

                            <span className="live-dot"></span>

                            Upcoming Event

                        </div>


                        {!loggedIn && (

                            <>
                                <h3>
                                    GECCS Tech Fest
                                </h3>

                                <p>
                                    Login to view upcoming events
                                    and participate in college activities.
                                </p>

                                <button
                                    className="card-button"
                                    onClick={() => {
                                        setShowLogin(true);
                                        setError("");
                                    }}
                                >
                                    Login to View
                                </button>
                            </>

                        )}


                        {loggedIn && loading && (

                            <h3>
                                Loading event...
                            </h3>

                        )}


                        {loggedIn &&
                            !loading &&
                            events.length > 0 && (

                                <>

                                    <h3>
                                        {events[0].title}
                                    </h3>

                                    <p>
                                        {events[0].description}
                                    </p>


                                    <div className="event-info">

                                        <div>

                                            <span>
                                                DATE
                                            </span>

                                            <strong>
                                                {formatDate(
                                                    events[0].startTime
                                                )}
                                            </strong>

                                        </div>


                                        <div>

                                            <span>
                                                VENUE
                                            </span>

                                            <strong>
                                                {events[0].venue}
                                            </strong>

                                        </div>

                                    </div>


                                    <button
                                        className="card-button"
                                        onClick={() =>
                                            handleViewEvent(events[0])
                                        }
                                    >
                                        View Event
                                    </button>

                                </>

                            )}


                        {loggedIn &&
                            !loading &&
                            events.length === 0 && (

                                <h3>
                                    No events available
                                </h3>

                            )}

                    </div>

                </section>


                {/* =========================
                    EVENTS
                ========================= */}

                <section
                    className="features"
                    id="events"
                >

                    <div className="section-heading">

                        <p>
                            LIVE FROM DATABASE
                        </p>

                        <h2>
                            Upcoming events.
                        </h2>

                    </div>


                    {!loggedIn && (

                        <p className="event-message">
                            Please login to view events.
                        </p>

                    )}


                    {loggedIn && loading && (

                        <p className="event-message">
                            Loading events...
                        </p>

                    )}


                    {loggedIn && error && (

                        <p className="event-message error">
                            {error}
                        </p>

                    )}


                    {loggedIn &&
                        !loading &&
                        !error &&
                        events.length === 0 && (

                            <p className="event-message">
                                No events found.
                            </p>

                        )}


                    {loggedIn &&
                        !loading &&
                        !error &&
                        events.length > 0 && (

                            <div className="feature-grid">

                                {events.map((event) => (

                                    <div
                                        className="feature-card"
                                        key={event.id}
                                    >

                                        <div className="feature-icon">
                                            EVENT
                                        </div>

                                        <h3>
                                            {event.title}
                                        </h3>

                                        <p>
                                            {event.description}
                                        </p>


                                        <div className="event-card-details">

                                            <span>
                                                {formatDate(
                                                    event.startTime
                                                )}
                                            </span>

                                            <span>
                                                {event.venue}
                                            </span>

                                        </div>


                                        <button
                                            className="event-view-button"
                                            onClick={() =>
                                                handleViewEvent(event)
                                            }
                                        >
                                            View Event
                                        </button>

                                    </div>

                                ))}

                            </div>

                        )}

                </section>


                {/* =========================
                    ABOUT
                ========================= */}

                <section
                    className="about"
                    id="about"
                >

                    <div>

                        <p className="eyebrow">
                            GECCS EVENT MANAGEMENT
                        </p>

                        <h2>
                            Bringing the campus
                            <br />
                            <span>
                                together.
                            </span>
                        </h2>

                    </div>


                    <p>
                        GECCS Event Management is designed to
                        centralize institutional events and make
                        participation easier for students while
                        giving student councils the tools they need
                        to manage events efficiently.
                    </p>

                </section>

            </main>


            {/* =========================
                FOOTER
            ========================= */}

            <footer>

                <div>

                    <strong>
                        GECCS
                    </strong>

                    <span>
                        {" "}Event Management System
                    </span>

                </div>

                <p>
                    Government College of Engineering,
                    Chhatrapati Sambhajinagar
                </p>

            </footer>


            {/* =========================
                LOGIN MODAL
            ========================= */}

            {showLogin && (

                <div
                    className="login-overlay"
                    onClick={() => setShowLogin(false)}
                >

                    <div
                        className="login-modal"
                        onClick={(event) =>
                            event.stopPropagation()
                        }
                    >

                        <button
                            className="close-button"
                            onClick={() =>
                                setShowLogin(false)
                            }
                        >
                            ×
                        </button>


                        <p className="eyebrow">
                            GECCS
                        </p>

                        <h2>
                            Welcome back.
                        </h2>

                        <p className="login-description">
                            Login using your official GECA
                            college email.
                        </p>


                        <form
                            onSubmit={handleLogin}
                        >

                            <label>
                                College Email
                            </label>

                            <input
                                type="email"
                                placeholder="yourname@geca.ac.in"
                                value={collegeEmail}
                                onChange={(event) =>
                                    setCollegeEmail(
                                        event.target.value
                                    )
                                }
                                required
                            />


                            <label>
                                Password
                            </label>

                            <input
                                type="password"
                                placeholder="Enter your password"
                                value={password}
                                onChange={(event) =>
                                    setPassword(
                                        event.target.value
                                    )
                                }
                                required
                            />


                            {error && (

                                <p className="login-error">
                                    {error}
                                </p>

                            )}


                            <button
                                type="submit"
                                className="login-submit"
                                disabled={loading}
                            >

                                {loading
                                    ? "Logging in..."
                                    : "Login"}

                            </button>

                        </form>

                    </div>

                </div>

            )}


            {/* =========================
                EVENT DETAILS MODAL
            ========================= */}

            {showEventDetails && selectedEvent && (

                <div
                    className="event-details-overlay"
                    onClick={closeEventDetails}
                >

                    <div
                        className="event-details-modal"
                        onClick={(event) =>
                            event.stopPropagation()
                        }
                    >

                        <button
                            className="event-details-close"
                            onClick={closeEventDetails}
                        >
                            ×
                        </button>


                        <div className="event-details-header">

                            <p className="eyebrow">
                                GECCS EVENT
                            </p>

                            <h2>
                                {selectedEvent.title}
                            </h2>

                            <p className="event-details-description">
                                {selectedEvent.description}
                            </p>

                        </div>


                        <div className="event-details-grid">

                            <div className="event-detail-item">

                                <span>
                                    DATE
                                </span>

                                <strong>
                                    {formatDate(
                                        selectedEvent.startTime
                                    )}
                                </strong>

                            </div>


                            <div className="event-detail-item">

                                <span>
                                    TIME
                                </span>

                                <strong>
                                    {formatTime(
                                        selectedEvent.startTime
                                    )}
                                    {" - "}
                                    {formatTime(
                                        selectedEvent.endTime
                                    )}
                                </strong>

                            </div>


                            <div className="event-detail-item">

                                <span>
                                    VENUE
                                </span>

                                <strong>
                                    {selectedEvent.venue ||
                                        "Venue not available"}
                                </strong>

                            </div>


                            <div className="event-detail-item">

                                <span>
                                    COUNCIL
                                </span>

                                <strong>
                                    {selectedEvent.councilCode ||
                                        "GECCS"}
                                </strong>

                            </div>


                            <div className="event-detail-item">

                                <span>
                                    REGISTRATION DEADLINE
                                </span>

                                <strong>
                                    {formatDateTime(
                                        selectedEvent.registrationDeadline
                                    )}
                                </strong>

                            </div>


                            <div className="event-detail-item">

                                <span>
                                    MAX PARTICIPANTS
                                </span>

                                <strong>
                                    {selectedEvent.maxParticipants ||
                                        "Not specified"}
                                </strong>

                            </div>

                        </div>


                        <div className="event-details-actions">

                            <button
                                className="event-register-button"
                                onClick={() => {
                                    alert(
                                        "Registration will be connected to the backend next."
                                    );
                                }}
                            >
                                Register Now
                            </button>

                            <button
                                className="event-cancel-button"
                                onClick={closeEventDetails}
                            >
                                Close
                            </button>

                        </div>

                    </div>

                </div>

            )}

        </div>
    );
}


/* =========================
   DATE FORMATTERS
========================= */

function formatDate(dateString) {

    if (!dateString) {
        return "Date not available";
    }

    const date = new Date(dateString);

    return date.toLocaleDateString(
        "en-IN",
        {
            day: "2-digit",
            month: "short",
            year: "numeric"
        }
    );
}


function formatTime(dateString) {

    if (!dateString) {
        return "Time not available";
    }

    const date = new Date(dateString);

    return date.toLocaleTimeString(
        "en-IN",
        {
            hour: "2-digit",
            minute: "2-digit"
        }
    );
}


function formatDateTime(dateString) {

    if (!dateString) {
        return "Not specified";
    }

    const date = new Date(dateString);

    return date.toLocaleString(
        "en-IN",
        {
            day: "2-digit",
            month: "short",
            year: "numeric",
            hour: "2-digit",
            minute: "2-digit"
        }
    );
}


export default App;