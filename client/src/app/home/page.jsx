"use client";
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";

export default function HomePage() {
    const [projects, setProjects] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);
    const [showModal, setShowModal] = useState(false);
    const [newProject, setNewProject] = useState({
        projectName: "",
        language: "Java",
        description: ""
    });

    const router = useRouter();

    useEffect(() => {
        // Check for token in URL params and save to localStorage if present
        const fetchToken = () => {
            const urlParams = new URLSearchParams(window.location.search);
            const token = urlParams.get("token");
            if (token) localStorage.setItem("token", token);
        };

        fetchToken();
        fetchProjects();
    }, []);

    const fetchProjects = async () => {
        setIsLoading(true);
        setError(null);

        try {
            const token = localStorage.getItem("token");
            if (!token) {
                router.push("/login");
                return;
            }

            const response = await fetch("http://localhost:8082/api/code-file", {
                headers: {
                    Authorization: `Bearer ${token}`,
                }
            });

            if (!response.ok) {
                throw new Error(`Failed to fetch projects: ${response.status}`);
            }

            const data = await response.json();
            console.log(data);

            // Remove duplicates by using a Map with project id as key
            const uniqueProjects = Array.from(
                new Map(data.map(project => [project.id, project])).values()
            );
            setProjects(uniqueProjects);
        } catch (err) {
            console.error("Error fetching projects:", err);
            setError("Failed to load projects. Please try again later.");
        } finally {
            setIsLoading(false);
        }
    };

    const handleCreateNewProject = () => {
        setShowModal(true);
        
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const token = localStorage.getItem("token");
            if (!token) {
                router.push("/login");
                return;
            }

            const response = await fetch("http://localhost:8082/api/code-file", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`,
                },
                body: JSON.stringify({
                    projectName: newProject.projectName,
                    language: newProject.language,
                    description: newProject.language // Using language as description as per API format
                })
            });

            if (!response.ok) {
                throw new Error(`Failed to create project: ${response.status}`);
            }

            // Refresh projects list
            fetchProjects();

            // Reset form and close modal
            setNewProject({ projectName: "", language: "Java", description: "" });
            setShowModal(false);
        } catch (err) {
            console.error("Error creating project:", err);
            alert("Failed to create project. Please try again.");
        }
    };

    const handleProjectClick = (projectId) => {
        console.log(projectId);
        router.push(`/project/${projectId}`);
    };

    if (isLoading) {
        return (
            <div className="flex flex-col items-center justify-center min-h-screen">
                <p className="text-xl">Loading your projects...</p>
            </div>
        );
    }

    return (
        <div className="flex flex-col items-center justify-center min-h-screen py-8 px-4">
            <h1 className="text-4xl font-bold mb-6">Your Projects</h1>

            <button
                className="bg-blue-600 text-white px-6 py-2 mb-6 rounded hover:bg-blue-700 transition"
                onClick={handleCreateNewProject}
            >
                + Create New Project
            </button>

            {error && (
                <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-6 w-full max-w-2xl">
                    {error}
                </div>
            )}

            <div className="w-full max-w-2xl space-y-4">
                {projects.length === 0 ? (
                    <p className="text-center text-gray-500">No projects found. Create a new one to get started.</p>
                ) : (
                    projects.map((project) => (
                        <div
                            key={project.id}
                            onClick={() => handleProjectClick(project.id)}
                            className="border p-4 rounded shadow hover:shadow-lg transition cursor-pointer"
                        >
                            <h2 className="text-2xl font-semibold">{project.projectName}</h2>
                            <p className="text-gray-700">Language: {project.language}</p>
                            <p className="text-gray-500 text-sm">Created: {new Date(project.createdAt).toLocaleDateString()}</p>
                        </div>
                    ))
                )}
            </div>

            {showModal && (
                <div className="fixed inset-0 bg-black bg-opacity-40 flex justify-center items-center z-50">
                    <div className="bg-white rounded-lg p-6 w-full max-w-md shadow-xl">
                        <h2 className="text-2xl font-semibold mb-4">Create New Project</h2>
                        <form onSubmit={handleSubmit} className="space-y-4">
                            <div>
                                <label className="block text-sm font-medium">Project Name</label>
                                <input
                                    type="text"
                                    value={newProject.projectName}
                                    onChange={(e) =>
                                        setNewProject({ ...newProject, projectName: e.target.value })
                                    }
                                    required
                                    className="w-full border rounded px-3 py-2 mt-1"
                                />
                            </div>

                            <div>
                                <label className="block text-sm font-medium">Language</label>
                                <select
                                    value={newProject.language}
                                    onChange={(e) =>
                                        setNewProject({
                                            ...newProject,
                                            language: e.target.value,
                                            description: e.target.value
                                        })
                                    }
                                    className="w-full border rounded px-3 py-2 mt-1"
                                >
                                    <option>Java</option>
                                    <option>C++</option>
                                    <option>C</option>
                                    <option>Python</option>
                                </select>
                            </div>

                            <div className="flex justify-end space-x-2 pt-4">
                                <button
                                    type="button"
                                    className="px-4 py-2 bg-gray-300 rounded hover:bg-gray-400"
                                    onClick={() => setShowModal(false)}
                                >
                                    Cancel
                                </button>
                                <button
                                    type="submit"
                                    className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
                                >
                                    Create
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
}