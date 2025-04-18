"use client";
import { useEffect, useState } from "react";
import ProfileInfo from "./ProfileInfo";
import ProfileEditForm from "./ProfileEditForm";

export default function ProfilePage() {
    const [user, setUser] = useState(null);
    const [error, setError] = useState(null);
    const [isEditing, setIsEditing] = useState(false);
    const [formData, setFormData] = useState({});

    useEffect(() => {
        const fetchUserData = async () => {
            const token = localStorage.getItem("token");
            console.log("#@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            console.log(token);


            if (!token) {
                setError("No token found. Please log in.");
                return;
            }

            try {
                const response = await fetch("http://localhost:8080/api/data/user-info", {
                    method: "GET",
                    headers: {
                        "Authorization": `Bearer ${token}`,
                        "Content-Type": "application/json",
                    },
                });

                if (!response.ok) {
                    throw new Error("Failed to fetch user info");
                }

                const data = await response.json();
                setUser(data);
                setFormData(data);
            } catch (err) {
                setError(err.message);
            }
        };

        fetchUserData();
    }, []);

    const handleEdit = () => setIsEditing(true);
    const handleChange = (e) => setFormData({ ...formData, [e.target.name]: e.target.value });

    const handleSave = async () => {
        const token = localStorage.getItem("token");
        if (!token) {
            setError("No token found. Please log in.");
            return;
        }

        try {
            await fetch("http://localhost:8080/api/data/edit-info", {
                method: "PUT",
                headers: {
                    "Authorization": `Bearer ${token}`,
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(formData),
            });

            setUser(formData);
            setIsEditing(false);
        } catch (err) {
            setError(err.message);
        }
    };

    return (
        <div className="flex flex-col items-center justify-center min-h-screen py-10 bg-gray-100">
            <div className="bg-white p-6 rounded-lg shadow-xl w-96 text-center">
                <h1 className="text-4xl font-bold text-blue-600 mb-4">Profile Page</h1>
                {error && <p className="text-red-500">{error}</p>}
                {user ? (
                    <div className="mt-4">
                        {isEditing ? (
                            <ProfileEditForm formData={formData} onChange={handleChange} onSave={handleSave} />
                        ) : (
                            <ProfileInfo user={user} onEdit={handleEdit} />
                        )}
                    </div>
                ) : (
                    !error && <p>Loading user information...</p>
                )}
            </div>
        </div>
    );
}
