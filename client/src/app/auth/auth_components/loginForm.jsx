"use client";
import { useState } from "react";
import { useRouter } from "next/navigation"; // Next.js 13+ router
import GoogleLoginButton from "./google";
import GithubLoginButton from "./github";

export default function LoginForm() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState(null);
    const router = useRouter(); // hook to navigate

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null);

        const requestData = { email, password };

        try {
            const response = await fetch("http://localhost:8080/api/auth/authenticate", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(requestData),
            });

            if (!response.ok) {
                throw new Error("Invalid credentials");
            }

            const data = await response.json();
            alert("Login successful!");
            localStorage.setItem("token", data.token);
        } catch (err) {
            setError(err.message);
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-gray-100 p-4">
            <div className="bg-white shadow-lg rounded-2xl p-8 flex flex-col md:flex-row gap-10 w-full max-w-4xl">
                {/* Login Form */}
                <form onSubmit={handleSubmit} className="flex-1 space-y-4">
                    <h2 className="text-2xl font-bold text-gray-800">Welcome Back</h2>

                    <div>
                        <label htmlFor="email" className="block text-sm font-medium text-gray-700">
                            Email
                        </label>
                        <input
                            type="email"
                            id="email"
                            className="mt-1 w-full px-4 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                            placeholder="Enter your email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                    </div>

                    <div>
                        <label htmlFor="password" className="block text-sm font-medium text-gray-700">
                            Password
                        </label>
                        <input
                            type="password"
                            id="password"
                            className="mt-1 w-full px-4 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                            placeholder="Enter your password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>

                    {error && <p className="text-red-500 text-sm">{error}</p>}

                    <button
                        type="submit"
                        className="w-full bg-blue-600 text-white py-2 px-4 rounded-lg hover:bg-blue-700 transition"
                    >
                        Login
                    </button>

                    <div className="text-sm text-center text-gray-600">
                        Don’t have an account?{" "}
                        <button
                            type="button"
                            onClick={() => router.push("/auth/register")}
                            className="text-blue-500 hover:underline"
                        >
                            Register here
                        </button>
                    </div>
                </form>

                {/* Divider */}
                <div className="hidden md:block w-px bg-gray-300"></div>

                {/* Social Login */}
                <div className="flex-1 flex flex-col items-center justify-center space-y-4">
                    <h3 className="text-gray-600 text-lg font-semibold">Or sign in with</h3>
                    <GoogleLoginButton />
                    <GithubLoginButton />
                </div>
            </div>
        </div>
    );
}
