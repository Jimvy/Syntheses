%% Notre fonction :
M = 2*pi*4; % Fréquence de coupure : 4 Hz
f = @(t) (1-cos(M*t))./(pi*M*t.^2);
% Sa transformée :
% F(j w) = 1 + w/M si -M < w < 0
%        = 1 - w/M si 0 < w < M
%        = 0       sinon
F1 = @(w) 1 + w./M;
F2 = @(w) 1 - w./M;
%% Première figure : signal x(t), x[n], x_\delta(t)
L = 2;
t = 0:0.001:L; % On prend sur 10 secondes
x = f(t);
fe = 10; % = 10 Hz
Te = 1/fe; % secondes
te = 0:Te:L;
xe = f(te)
x(1) = M/(2*pi); % correction
xe(1) = x(1);
figure; hold on;
plot(t, x, '-');
stem(te, xe, '--', 'Marker', '^');
xlabel('t');

%% Deuxième figure : les transformées / DFT / DTFT
w = linspace(-2*pi*20, 2*pi*20, 1000);
X = zeros(size(w));
for i=1:length(w)
    if (0 <= w(i) && w(i) <= M)
        X(i) = F2(w(i));
    elseif (-M <= w(i) && w(i) < 0)
        X(i) = F1(w(i));
    % else : 0
    end
end
we = linspace(0, fe, length(xe)+1); we(end)=[];
Xe = fft(xe)
figure; hold on;
plot(w, X, '-');
plot(we, Xe, '--');