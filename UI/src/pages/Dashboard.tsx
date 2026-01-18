import React, { Suspense, lazy } from 'react';
import { useAuth } from '../context/AuthContext';

// Lazy load dashboards for better performance
const AdminDashboard = lazy(() => import('./admin/AdminDashboard'));
const MedicoDashboard = lazy(() => import('./medico/MedicoDashboard'));
const PacienteDashboard = lazy(() => import('./paciente/PacienteDashboard'));

const DashboardResolver: React.FC = () => {
    const { user } = useAuth();
    const role = user?.role;

    return (
        <Suspense fallback={
            <div className="flex items-center justify-center min-h-[60vh]">
                <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-medical-500"></div>
            </div>
        }>
            {role === 'ROLE_ADMIN' && <AdminDashboard />}
            {role === 'ROLE_MEDICO' && <MedicoDashboard />}
            {role === 'ROLE_USER' && <PacienteDashboard />}
        </Suspense>
    );
};

export default DashboardResolver;
