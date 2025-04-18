import Link from "next/link";

export default function Header() {
    return (
        <div className="bg-blue-500 p-4 text-white text-center flex justify-between items-center">
            <h1 className="text-2xl">Welcome to the Code Editor</h1>
            <Link href="/profile">
                <button className="bg-white text-blue-500 px-4 py-2 rounded hover:bg-gray-200">
                    Profile
                </button>
            </Link>

    
        </div>

    );
}
