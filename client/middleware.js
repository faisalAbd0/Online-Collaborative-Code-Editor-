import { NextResponse } from 'next/server';

export function middleware(request) {
    console.log('Middleware is running');
    return NextResponse.next();
}

// Apply middleware to all routes
// export const config = {
//     matcher: '/*', // Ensures middleware runs on all routes
// };
