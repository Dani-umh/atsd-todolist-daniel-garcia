ALTER TABLE public.equipos
ADD COLUMN descripcion character varying(255);

UPDATE public.equipos
SET descripcion = ''
WHERE descripcion IS NULL;

ALTER TABLE public.equipos
ALTER COLUMN descripcion SET NOT NULL;