"use client";
import { useEffect, useState } from "react";
import { useParams, useRouter } from "next/navigation";
import CodeEditor from "@/app/compiler/page";

export default function ProjectPage() {
    const params = useParams();
    const projectId = params.id;
    const router = useRouter();

    const [project, setProject] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);
    const [collaborators, setCollaborators] = useState([]);
    
    useEffect(() => {
        async function fetchAllData() {
            console.log("Fetching project info...");

            const token = localStorage.getItem("token");
            if (!token) {
                router.push("/login");
                return;
            }

            try {
                // Fetch project data
                const projectRes = await fetch(`http://localhost:8082/api/code-file/project/${projectId}`, {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });

                if (!projectRes.ok) {
                    throw new Error(`Failed to fetch project info: ${projectRes.status}`);
                }

                const projectData = await projectRes.json();
                setProject(projectData);

                // Fetch collaborators
                const collabRes = await fetch(`http://localhost:8082/api/code-file/${projectId}/collaborators`);

                if (!collabRes.ok) {
                    throw new Error(`Failed to fetch collaborators`);
                }

                const collabList = await collabRes.json();
                setCollaborators(collabList);

                console.log("Project info and collaborators fetched");
            } catch (err) {
                console.error("Error fetching data:", err);
                setError(err.message);
            } finally {
                setIsLoading(false);
            }
        }

        fetchAllData();
    }, [projectId, router]);

    if (isLoading) {
        return (
            <div className="flex justify-center items-center h-screen">
                <p className="text-xl">Loading project...</p>
            </div>
        );
    }

    if (error) {
        return (
            <div className="flex justify-center items-center h-screen">
                <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded max-w-md">
                    <p className="font-bold">Error loading project</p>
                    <p>{error}</p>
                    <button
                        onClick={() => router.push("/")}
                        className="mt-2 bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
                    >
                        Return to Dashboard
                    </button>
                </div>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gray-100">
            <nav className="bg-white shadow-sm p-4">
                <div className="container mx-auto flex justify-between items-center">
                    <div className="flex items-center">
                        <button
                            className="mr-4 text-blue-600 hover:text-blue-800"
                            onClick={() => router.push("/")}
                        >
                            ← Back to Projects
                        </button>
                        <h1 className="text-xl font-bold">{project?.projectName || "Project"}</h1>
                    </div>
                    <div className="text-sm text-gray-600">
                        <span className="mr-4">Language: {project?.language}</span>
                        <span>Project ID: {projectId}</span>
                    </div>
                </div>
            </nav>

            <div className="container mx-auto p-4">
                {/* Collaborators */}
                <div className="bg-white p-4 rounded shadow mb-4">
                    <h2 className="text-lg font-semibold mb-2">Collaborators</h2>
                    {collaborators.length > 0 ? (
                        <ul className="list-disc ml-5 text-sm text-gray-700">
                            {collaborators.map((collab, idx) => (
                                <li key={idx}>{collab.userIdentifier}</li>
                            ))}
                        </ul>
                    ) : (
                        <p className="text-gray-500 text-sm">No collaborators added yet.</p>
                    )}
                </div>

                {/* Code Editor */}
                <CodeEditor project={project} projectId={projectId} />
            </div>
        </div>
    );
}
