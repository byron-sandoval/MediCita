import React, { createContext, useContext, useState, useEffect } from 'react';
import type { Role } from '../types';
import keycloak from '../keycloak';

interface User {
    id: string;
    name: string;
    email: string;
    role: Role;
}

interface AuthContextType {
    user: User | null;
    isAuthenticated: boolean;
    login: () => void;
    logout: () => void;
    token: string | null;
    keycloakReady: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    const [user, setUser] = useState<User | null>(null);
    const [token, setToken] = useState<string | null>(null);
    const [ready, setReady] = useState(false);

    useEffect(() => {
        keycloak.init({
            onLoad: 'check-sso',
            silentCheckSsoRedirectUri: window.location.origin + '/silent-check-sso.html',
            pkceMethod: 'S256'
        }).then((authenticated) => {
            if (authenticated) {
                const roles = keycloak.realmAccess?.roles || [];
                let role: Role = 'ROLE_USER'; // Default role

                if (roles.includes('ROLE_ADMIN')) role = 'ROLE_ADMIN';
                else if (roles.includes('ROLE_MEDICO')) role = 'ROLE_MEDICO';

                const userData: User = {
                    id: keycloak.subject || 'unknown',
                    name: keycloak.tokenParsed?.name || keycloak.tokenParsed?.preferred_username || 'User',
                    email: keycloak.tokenParsed?.email || '',
                    role: role
                };

                setUser(userData);
                setToken(keycloak.token || null);
                localStorage.setItem('token', keycloak.token || '');
            }
            setReady(true);
        }).catch((err) => {
            console.error("Keycloak init error", err);
            setReady(true);
        });

        keycloak.onTokenExpired = () => {
            keycloak.updateToken(30).then((refreshed) => {
                if (refreshed) {
                    setToken(keycloak.token || null);
                    localStorage.setItem('token', keycloak.token || '');
                }
            });
        };
    }, []);

    const login = () => {
        keycloak.login();
    };

    const logout = () => {
        keycloak.logout({ redirectUri: window.location.origin });
        setUser(null);
        setToken(null);
        localStorage.removeItem('token');
    };

    return (
        <AuthContext.Provider value={{
            user,
            isAuthenticated: !!user,
            login,
            logout,
            token,
            keycloakReady: ready
        }}>
            {ready ? children : (
                <div className="min-h-screen flex items-center justify-center bg-slate-50">
                    <div className="flex flex-col items-center space-y-4">
                        <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-medical-500"></div>
                        <p className="text-slate-400 font-medium font-serif italic">Iniciando seguridad MediCita...</p>
                    </div>
                </div>
            )}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (context === undefined) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
};
