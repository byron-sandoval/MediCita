import React from 'react';
import { Settings, UserCircle, Construction } from 'lucide-react';

const PlaceholderPage: React.FC<{ title: string }> = ({ title }) => (
    <div className="flex flex-col items-center justify-center min-h-[60vh] text-center space-y-6 animate-in fade-in duration-700">
        <div className="w-24 h-24 bg-medical-50 rounded-3xl flex items-center justify-center shadow-inner">
            <Construction className="w-12 h-12 text-medical-500 animate-bounce" />
        </div>
        <div>
            <h2 className="text-3xl font-bold text-slate-800 italic">{title}</h2>
            <p className="text-slate-500 mt-2 max-w-md mx-auto">
                Estamos trabajando arduamente para brindarte la mejor experiencia en este módulo. ¡Vuelve pronto!
            </p>
        </div>
        <div className="pt-4 flex gap-3">
            <div className="w-2 h-2 rounded-full bg-medical-200"></div>
            <div className="w-2 h-2 rounded-full bg-medical-400"></div>
            <div className="w-2 h-2 rounded-full bg-medical-600"></div>
        </div>
    </div>
);

export default PlaceholderPage;
