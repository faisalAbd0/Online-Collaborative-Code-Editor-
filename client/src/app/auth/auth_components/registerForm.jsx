"use client";

import { useState } from "react";
import GoogleLoginButton from "./google";
import GithubLoginButton from "./github";

export default function RegisterForm() {
    const [formData, setFormData] = useState({
        firstName: "",
        lastName: "",
        userIdentifier: "",
        email: "",
        password: ""
    });

    const [error, setError] = useState(null);

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        setError(null);

        try {
            const response = await fetch("http://localhost:8080/api/auth/register", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(formData)
            });

            const result = await response.json();
            if (response.ok) {
                console.log("Registration successful:", result);
                alert("Registration successful!");
                localStorage.setItem("token", result.token);
            } else {
                console.error("Registration failed:", result);
                setError(result.message || "Registration failed. Please try again.");
            }
        } catch (error) {
            console.error("Error during registration:", error);
            setError("An error occurred. Please try again.");
        }
    };

    const labels = {
        firstName: "First Name",
        lastName: "Last Name",
        userIdentifier: "Username",
        email: "Email",
        password: "Password"
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-gray-100 p-4">
            <div className="bg-white shadow-lg rounded-2xl p-8 flex flex-col md:flex-row gap-10 w-full max-w-4xl">
                {/* Form Section */}
                <form onSubmit={handleSubmit} className="flex-1 space-y-4">
                    <h2 className="text-2xl font-bold text-gray-800">Create an Account</h2>

                    {Object.keys(formData).map((field) => (
                        <div key={field}>
                            <label htmlFor={field} className="block text-sm font-medium text-gray-700">
                                {labels[field]}
                            </label>
                            <input
                                type={field === "email" ? "email" : field === "password" ? "password" : "text"}
                                id={field}
                                name={field}
                                value={formData[field]}
                                onChange={handleChange}
                                placeholder={`Enter your ${labels[field].toLowerCase()}`}
                                className="mt-1 block w-full px-4 py-2 border border-gray-300 rounded-lg shadow-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none"
                                required
                            />
                        </div>
                    ))}

                    {error && <p className="text-red-500 text-sm">{error}</p>}

                    <button
                        type="submit"
                        className="w-full bg-blue-600 text-white py-2 px-4 rounded-lg hover:bg-blue-700 transition"
                    >
                        Register
                    </button>
                </form>

                {/* Divider */}
                <div className="hidden md:block w-px bg-gray-300"></div>

                {/* Social Login */}
                <div className="flex-1 flex flex-col items-center justify-center space-y-4">
                    <h3 className="text-gray-600 text-lg font-semibold">Or sign up with</h3>
                    <GoogleLoginButton />
                    <GithubLoginButton />
                </div>
            </div>
        </div>
    );
}
