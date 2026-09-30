-- Restore the existing server-side booking API permissions.
-- RLS remains enabled and no client role receives data access.
BEGIN;
GRANT SELECT, INSERT ON public.bookings, public.status_history, public.notifications TO service_role;
COMMIT;
