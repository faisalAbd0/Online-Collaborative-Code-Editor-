"use client";
import { useEffect, useState } from "react";
import { useParams, useRouter } from "next/navigation";
import CodeEditor from "@/app/compiler/page";
export default function ProjectPage() {
    const params = useParams();
    const projectId = params.id;
    const [project, setProject] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);

    const router = useRouter();

    useEffect(() => {
        console.log("Fetching project info...");

        fetchProjectInfo();

        console.log("Project info fetched");

    }, [projectId]);

    const fetchProjectInfo = async () => {
        setIsLoading(true);
        try {
            const token = localStorage.getItem("token");
            if (!token) {
                router.push("/login");
                return;
            }
            console.log(projectId);

            const response = await fetch(`http://localhost:8082/api/code-file/project/${projectId}`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                }
            });

            if (!response.ok) {
                throw new Error(`Failed to fetch project info: ${response.status}`);
            }

            const data = await response.json();
            console.log(data);

            setProject(data);
        } catch (err) {
            console.error("Error fetching project:", err);
            setError(err.message);
        } finally {
            setIsLoading(false);
        }
    };

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

            <div className="container mx-auto">
                <CodeEditor
                    project={project}
                    projectId={projectId} />
            </div>
        </div>
    );
}